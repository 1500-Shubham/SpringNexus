# Theory
Ollama provides large language models (LLMs) that are typically hosted locally, designed to understand and generate human-like text. Runs Locally: No dependency on cloud services. Privacy: Data doesn't leave your local system

## Prompt
A "prompt" is the input or context given to a model to generate meaningful output.
System Prompt: Sets the behavior of the AI. It defines how the model should respond.
Example: "You are a friendly assistant who provides concise answers."

User Prompt: The input provided by the user to the AI.
Example: "What is the capital of France?"

Combined Prompt: Models take the system prompt and user prompt together to generate a reply.

## Mathematics Behind Training:
Models use functions like softmax and backpropagation to minimize prediction errors.
For example, predicting the next word in "The sky is __" involves probabilities:

## Implementation Terminal

### Docker  
- Pull- docker pull ghcr.io/ollama/ollama:latest
- Run- docker run -it --rm -p 11434:11434 ghcr.io/ollama/ollama 
Ollama service on your localhost at port 11434.
- Command <br>
curl http://localhost:11434/api/generate -X POST -H "Content-Type: application/json" \
-d '{
    "model": "llama2",
    "system": "You are a helpful assistant.",
    "prompt": "What is the capital of France?"
}'

### Local Setup
- brew install ollama
- ollama --version
- ollama list
- ollama pull llama2
- ollama delete llama2
- ollama stop llama2 //otherwise run karta rahega background mein
- ollama run llama2
- ollama run llama2 "system prompt + userPromt" // single query run and get output

### Ollama serve https://github.com/ollama/ollama/blob/main/docs/api.md#list-local-models
Start ollama as a local server and can communicate with using API
- Get Models 
curl http://localhost:11434/api/tags 
- Pull Models  
curl http://localhost:11434/api/pull -d '{
  "model": "llama2"  
}'
- Delete Models
curl -X DELETE http://localhost:11434/api/delete -d '{
  "model": "llama3:13b"
}'
- Generate response
curl http://localhost:11434/api/generate \ 
  -H "Content-Type: application/json" \
  -d '{
    "model": "llama2",
    "prompt": "Why is the sky blue? || Here only add system + user prompt",
    "stream": false
  }'

## Models Ollama Supports
- LLaMA
- Mistral
- StableLM

