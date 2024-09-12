# Aplicação de Ecommerce

## Visão Geral

Este projeto é um sistema de ecommerce desenvolvido em Java 17+ com Spring Boot, projetado para gerenciar produtos, estoque, vendas e usuários. A aplicação segue boas práticas de desenvolvimento e está organizada utilizando **estrutura em camadas**, garantindo a manutenibilidade e escalabilidade. O sistema integra-se com um banco de dados relacional (h2 ou MySQL), utilizando cache eficiente e mecanismos de autenticação/autorização de usuários.

## Funcionalidades

### 1. Gestão de Produtos
- **Validação**: O preço do produto deve ser positivo, e outras validações garantem a integridade dos dados.
- **Inativação de Produtos**: Produtos não podem ser excluídos após serem vendidos, mas podem ser inativados.

### 2. Controle de Estoque
- O sistema garante que os produtos não possam ser vendidos caso o estoque seja insuficiente.

### 3. Gestão de Vendas
- **Operações CRUD**: Usuários podem gerenciar vendas, sendo que uma venda deve conter pelo menos um produto para ser concluída.

### 4. Relatórios
- Relatórios de vendas podem ser gerados por data, por mês ou pela semana atual (considerando dias úteis).

### 5. Cache
- As requisições GET para produtos e vendas são armazenadas em cache para melhorar o desempenho.
- O cache é invalidado quando uma nova venda é realizada ou há atualizações para garantir a consistência dos dados.

### 6. Tratamento de Exceções
- Todas as exceções são tratadas de maneira consistente, seguindo um padrão unificado de resposta.

### 7. Formato de Data
- Todos os campos de data seguem o padrão ISO 8601 (exemplo: 2023-07-20T12:00:00Z).

### 8. Autenticação e Autorização
- **Autenticação JWT**: O sistema utiliza JWT para autenticação segura dos usuários.
- **Controle de Acesso Baseado em Funções**: Apenas usuários com permissão de `ADMIN` podem excluir informações ou gerenciar produtos e outros usuários `ADMIN`.

### 9. Redefinição de Senha
- A funcionalidade de redefinição de senha permite que os usuários alterem suas senhas de forma segura.

## Tecnologias

- **Java 17+**
- **Spring Boot**
- **h2/MySQL**
- **JWT para Autenticação**
- **ehCache**
- **GitHub para versionamento**

## Modelo de Banco de Dados

O sistema utiliza um banco de dados relacional modelado via diagramas **ER**, garantindo o relacionamento claro entre as principais entidades como `Produto`, `Usuário`, `Grupo`,`Venda`,`Carrinho`.
