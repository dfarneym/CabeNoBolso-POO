# Cabe no Bolso — Planejador de Despesas Compartilhadas

Projeto desenvolvido em Java para a atividade final da disciplina de Programação Orientada a Objetos da Universidade de Fortaleza (Unifor), período 2026.2.

## Sobre o projeto

O Cabe no Bolso é uma aplicação que ajuda a organizar despesas compartilhadas em atividades de grupo, como passeios e viagens. O usuário pode cadastrar participantes, definir seus orçamentos, registrar despesas e acompanhar quanto cada pessoa deverá pagar.

O sistema também informa quando um participante ultrapassa o orçamento disponível.

## Funcionalidades

- Cadastro e atualização de participantes.
- Cadastro, edição e exclusão de despesas.
- Despesas fixas divididas entre participantes selecionados.
- Despesas por pessoa, com valor individual.
- Cálculo do gasto total e dos valores individuais.
- Identificação de orçamento excedido.
- Salvamento e carregamento do planejamento em arquivo local.
- Interface gráfica desenvolvida com Java Swing.

## Tecnologias utilizadas

- Java
- Java Swing
- Programação Orientada a Objetos
- Arquitetura MVC
- Serialização de objetos
- Git e GitHub

## Requisitos

Para compilar e executar a aplicação, é necessário ter o JDK instalado e os comandos `java` e `javac` disponíveis no terminal.

A aplicação utiliza recursos da biblioteca padrão do Java e não precisa de bibliotecas externas.

## Como executar

Abra o terminal dentro da pasta `CabeNoBolso`, onde está localizada a pasta `src`.

**1. Compilar o projeto**

```powershell
javac -encoding UTF-8 -d bin src/model/*.java src/persistence/*.java src/controller/*.java src/view/*.java src/Main.java
```

**2. Executar a aplicação**

```powershell
java -cp bin Main
```

A janela Cabe no Bolso será aberta com as abas Participantes, Despesas e Resumo.

## Como executar os testes

No terminal, dentro da mesma pasta, compile o projeto e as classes de teste:

```powershell
javac -encoding UTF-8 -d bin src/model/*.java src/persistence/*.java src/controller/*.java src/view/*.java src/testes/*.java src/Main.java
```

Execute as classes de teste:

```powershell
java -cp bin model.TestePlanejamento
java -cp bin testes.TesteController
java -cp bin testes.TestePersistencia
java -cp bin testes.TesteIntegracaoCompleta
java -cp bin testes.TesteValidacaoDespesas
```

Os testes verificam os cálculos, as validações, as operações do Controller e o salvamento e carregamento dos dados.

Os testes automatizados foram executados após a integração dos módulos, sem apresentar falhas. Foram verificados os cadastros, as alterações, os cálculos das despesas, o tratamento de dados inválidos e a recuperação do planejamento.

Também realizamos testes pela interface gráfica, confirmando o funcionamento das abas Participantes, Despesas e Resumo.

## Exemplo de funcionamento

Para verificar os cálculos, utilizamos o caso de referência da atividade, com três participantes e quatro despesas compartilhadas: transporte, almoço, atividade recreativa e sobremesa.

Os resultados obtidos foram:

| Participante | Orçamento | Gasto | Situação |
|---|---:|---:|---|
| Ana | R$ 70,00 | R$ 75,00 | Excedeu R$ 5,00 |
| Bruno | R$ 60,00 | R$ 60,00 | Dentro do orçamento |
| Carla | R$ 70,00 | R$ 75,00 | Excedeu R$ 5,00 |

**Total geral: R$ 210,00.**

Os mesmos resultados foram confirmados após salvar e carregar o planejamento.

## Salvamento dos dados

A aplicação permite salvar o planejamento pelo botão **Salvar planejamento** e recuperá-lo pelo botão **Carregar planejamento**.

O arquivo utilizado é `planejamento.dat`, criado na pasta de execução do programa.

## Organização do projeto

- `src/model/`: classes e regras de negócio.
- `src/controller/`: comunicação entre a interface e o Model.
- `src/view/`: interface gráfica Swing.
- `src/persistence/`: salvamento e carregamento.
- `src/testes/`: testes das funcionalidades.
- `src/Main.java`: inicialização da aplicação.
- `docs/uml/UML-CabenoBolso.mdj`: arquivo editável do diagrama de classes, desenvolvido no StarUML.
- `docs/uml/Main.svg`: versão exportada do diagrama UML, utilizada na documentação.

## Equipe

O desenvolvimento do Cabe no Bolso foi realizado por Daniel, Marcos e Yasmin, com responsabilidades distribuídas entre modelagem UML, regras de negócio, interface gráfica, persistência, integração e testes. Sara participou das reuniões de acompanhamento e alinhamento da equipe e documentação.

O projeto foi organizado com apoio do Trello, GitHub, Google Meet e WhatsApp.

## Repositório

https://github.com/dfarneym/CabeNoBolso-POO