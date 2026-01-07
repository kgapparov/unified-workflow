# Пример настройки Basic LLM Chain в n8n

## Сценарий использования:
Пользователь общается через **Admin Chat** или **DeepSeek Chat**, запрос анализируется **Basic LLM Chain**, результат парсится и используется для дальнейших действий.

## Шаги настройки в n8n:

### 1. **Создание Basic LLM Chain**
1. В n8n добавьте узел **"Basic LLM Chain"**
2. Настройте параметры:
   - **LLM Provider**: DeepSeek
   - **Model**: deepseek-chat
   - **Temperature**: 0.3
   - **Max Tokens**: 2000

### 2. **Настройка System Prompt**
В поле **"System Prompt"** скопируйте следующий текст:

```
Ты - AI архитектор системы unified-workflow (оркестратор workflow). Твоя задача анализировать запросы пользователей о разработке, улучшении или исправлении системы.

## Контекст системы:
Проект: unified-workflow - система оркестрации workflow с AI-агентами.

Компоненты:
1. workflow-api (REST API, порт 8080)
2. workflow-engine (движок исполнения)
3. workflow-primitive (примитивы операций)
4. workflow-common (общие модели)
5. workflow-queue (очередь задач)
6. workflow-registry (реестр workflow)
7. workflow-service-client (клиент)

AI-агенты: Architect, TDD-Tester, Implementer, Docker-Packager.

## Инструкции:
- Определи тип задачи: feature, bug, improvement
- Определи затронутые компоненты
- Оцени сложность: low, medium, high
- Предложи рекомендуемого агента
- ВСЕГДА возвращай ТОЛЬКО JSON в формате:

{
  "task_type": "feature|bug|improvement",
  "components": ["component1", "component2"],
  "subtasks": ["подзадача1", "подзадача2"],
  "recommended_agent": "architect|tdd-tester|implementer|docker-packager",
  "complexity": "low|medium|high",
  "estimated_time": "2 hours",
  "notes": "технические заметки",
  "code_references": [{"file": "путь/к/файлу", "purpose": "описание"}]
}
```

### 3. **Настройка User Input**
- **Input Source**: "From previous node" или "Manual"
- **User Message**: `{{$json.message}}` (если приходит из чата)

### 4. **Добавление Code Node для парсинга ответа**
После Basic LLM Chain добавьте узел **"Code"** с таким JavaScript кодом:

```javascript
// Parse LLM response
const llmResponse = $input.first().json;

// Extract JSON from response (LLM might return text with JSON)
let analysis = {};
let rawResponse = '';

if (typeof llmResponse === 'string') {
  rawResponse = llmResponse;
  try {
    // Try to extract JSON from text
    const jsonMatch = llmResponse.match(/\{.*\}/s);
    if (jsonMatch) {
      analysis = JSON.parse(jsonMatch[0]);
    }
  } catch (e) {
    analysis = { error: 'Failed to parse JSON', raw: llmResponse };
  }
} else if (llmResponse.content) {
  rawResponse = llmResponse.content;
  try {
    analysis = JSON.parse(llmResponse.content);
  } catch (e) {
    analysis = { error: 'Failed to parse content', raw: llmResponse.content };
  }
} else {
  analysis = llmResponse;
}

// Prepare data for next steps
const result = {
  originalRequest: $input.first().json.message || 'No message',
  llmRawResponse: rawResponse,
  analysis: analysis,
  timestamp: new Date().toISOString(),
  workflowId: `analysis-${Date.now()}`
};

// Log for debugging
console.log('LLM Analysis Result:', JSON.stringify(result, null, 2));

return [{ json: result }];
```

### 5. **Маршрутизация к агентам**
Добавьте еще один **Code Node** для маршрутизации:

```javascript
const data = $input.first().json;
const analysis = data.analysis;

// Default values if analysis failed
const agent = analysis.recommended_agent || 'architect';
const taskType = analysis.task_type || 'feature';

// Prepare agent data
const agentData = {
  originalRequest: data.originalRequest,
  analysis: analysis,
  workflowId: data.workflowId,
  timestamp: data.timestamp
};

// Determine next steps
let nextAction = '';
let message = '';

switch (agent) {
  case 'architect':
    nextAction = 'call_architect_workflow';
    message = 'Задача передана архитектору для проектирования workflow';
    break;
  case 'tdd-tester':
    nextAction = 'call_tdd_tester';
    message = 'Задача передана TDD-тестировщику для генерации тестов';
    break;
  case 'implementer':
    nextAction = 'call_implementer';
    message = 'Задача передана имплементатору для написания кода';
    break;
  case 'docker-packager':
    nextAction = 'call_docker_packager';
    message = 'Задача передана Docker-упаковщику';
    break;
  default:
    nextAction = 'manual_review';
    message = 'Требуется ручной review задачи';
}

return [{
  json: {
    ...data,
    routing: {
      agent: agent,
      taskType: taskType,
      nextAction: nextAction,
      message: message,
      agentData: agentData
    }
  }
}];
```

### 6. **Интеграция с Chat**
Чтобы вернуть ответ в чат, добавьте узел, который форматирует ответ:

```javascript
const data = $input.first().json;
const analysis = data.analysis;
const routing = data.routing;

// Format response for chat
const chatResponse = `
🎯 **Анализ задачи завершен!**

📋 **Тип задачи:** ${analysis.task_type || 'Не определен'}
🏗️ **Компоненты:** ${analysis.components ? analysis.components.join(', ') : 'Не определены'}
⚡ **Сложность:** ${analysis.complexity || 'Не определена'}
⏱️ **Оценка времени:** ${analysis.estimated_time || 'Не определена'}

🤖 **Рекомендованный агент:** ${routing.agent}
📝 **Действие:** ${routing.message}

${analysis.notes ? `📌 **Заметки:** ${analysis.notes}` : ''}

${analysis.code_references ? `🔗 **Ссылки на код:** ${JSON.stringify(analysis.code_references, null, 2)}` : ''}
`;

return [{
  json: {
    chatResponse: chatResponse,
    rawData: data,
    timestamp: new Date().toISOString()
  }
}];
```

## Пример полного workflow:

1. **Trigger**: Admin Chat / DeepSeek Chat (получает сообщение пользователя)
2. **Basic LLM Chain**: Анализ запроса с системным промптом
3. **Code Node 1**: Парсинг JSON ответа от LLM
4. **Code Node 2**: Маршрутизация к агенту
5. **Code Node 3**: Форматирование ответа для чата
6. **Respond**: Возврат ответа в чат

## Тестирование:

### Тестовый запрос через чат:
```
"Добавить аутентификацию через OAuth2 в workflow-api"
```

### Ожидаемый ответ LLM:
```json
{
  "task_type": "feature",
  "components": ["workflow-api", "auth"],
  "subtasks": ["Добавить OAuth2 конфигурацию", "Создать endpoint", "Интегрировать с примитивами"],
  "recommended_agent": "architect",
  "complexity": "medium",
  "estimated_time": "4 hours",
  "notes": "Нужно добавить spring-security-oauth2",
  "code_references": [{"file": "workflow-api/src/main/java/com/uwf/workflow/api/config/PrimitivesConfig.java", "purpose": "Конфигурация примитивов"}]
}
```

### Ответ в чате:
```
🎯 **Анализ задачи завершен!**

📋 **Тип задачи:** feature
🏗️ **Компоненты:** workflow-api, auth
⚡ **Сложность:** medium
⏱️ **Оценка времени:** 4 hours

🤖 **Рекомендованный агент:** architect
📝 **Действие:** Задача передана архитектору для проектирования workflow

📌 **Заметки:** Нужно добавить spring-security-oauth2

🔗 **Ссылки на код:** [
  {
    "file": "workflow-api/src/main/java/com/uwf/workflow/api/config/PrimitivesConfig.java",
    "purpose": "Конфигурация примитивов"
  }
]
```

## Следующие шаги:

1. **Настройте Basic LLM Chain** с предоставленным системным промптом
2. **Протестируйте** с разными запросами
3. **Добавьте реальных агентов** (можно начать с существующего Architect workflow)
4. **Настройте интеграцию** между чатом и workflow
