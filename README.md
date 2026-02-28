# OrderFlow

Um sistema de gerenciamento de fluxo de pedidos e controle de usuários.

O projeto adota a separação de responsabilidades. O Back-end atua como uma API independente que fornece os dados e regras de negócio, 
enquanto o Front-end consome esses serviços e reage a eventos do servidor.

---

## Back-end (API RESTful)

Responsável pelo processamento, persistência de dados, segurança e mensageria assíncrona. (WIP)

### Tecnologias e Ferramentas

- Spring Boot  
- Spring Security & JWT  
- PostgreSQL  
- RabbitMQ  
- SSE (Server-Sent Events)  
- Lombok  
- Hibernate / JPA  

---

## Front-end (Aplicação Web)

Interface reativa do usuário, responsável pela interação, armazenamento seguro de tokens e escuta de eventos do servidor. (WIP)

O foco principal de estudo e arquitetura deste projeto é o Back-end. Essa interface do usuário está sendo desenvolvida 
de forma experimental com o auxílio de Agentes de IA. O objetivo desta camada é aplicar técnicas de Engenharia 
de Prompt para entender a interação e como as IAs geram, estruturam e integram código de interface para consumir uma API RESTful completa.

### Tecnologias e Ferramentas

- Vue.js  
- Tailwind CSS  

---

## Organização do Projeto

Cada pasta principal (Back-end e Front-end) vai possuir um README próprio, onde são explicadas de forma mais detalhada:
- A estrutura interna do projeto
- As responsabilidades de cada camada ou módulo
- O funcionamento e o papel de cada parte do sistema
