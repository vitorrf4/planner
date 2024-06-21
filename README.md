# Planner App / Website

Este repositório contém um sistema completo de gerenciamento de tarefas, composto por um aplicativo Android desenvolvido em Kotlin, uma página web HTML para interface de usuário e uma API em Node.js que conecta o aplicativo e a página web, permitindo atualizações em tempo real com Server Side Rendering (SSR).

## Funcionalidades
**Criação de Tarefas**: Os usuários podem criar novas tarefas especificando título, descrição e prazo.

**Visualização de Tarefas**: Listagem de todas as tarefas pendentes e concluídas.

**Atualização de Tarefas**: Edição das informações das tarefas existentes, incluindo marcação de conclusão.

**Exclusão de Tarefas**: Remoção de tarefas existentes na lista.

**Atualização em Tempo Real**: Utilização de Server Side Rendering na API para garantir que as mudanças feitas no aplicativo ou na página web sejam refletidas imediatamente.

## Componentes do Projeto
**Aplicativo Android (Kotlin):**

Desenvolvido para oferecer uma interface intuitiva para gerenciar tarefas diretamente do dispositivo móvel.
Utiliza conexões com a API para sincronizar dados e atualizações.

**Página Web HTML:**

Interface acessível via navegador que permite visualizar e gerenciar tarefas.
Integrada à API para renderização dinâmica de conteúdo e atualizações em tempo real.

**API em Node.js:**

Responsável por fornecer endpoints RESTful para manipulação de tarefas.
Utiliza SSR para gerenciar as conexões e processar solicitações tanto da página web quanto do aplicativo.
