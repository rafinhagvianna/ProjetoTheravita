# PROJETO SENAI
## Este projeto gere uma empresa farmacêutica

# INTEGRANTES:

- Gabriel Bachega 
- Gabriel Oliveira Mendes Silva
- Luiz Fernando Moreira Domênico
- Rafael de Godoy Vianna
- Rafael de Palma Francisco



# DIAGRAMA DE CLASSES #





![image](https://github.com/user-attachments/assets/07f8db8c-b71f-43e3-97fa-a08cabf5116f)

<a target:_blank href="https://lucid.app/lucidchart/4dadb130-d460-4170-ab61-7ca0cf6d8236/edit?viewport_loc=-1917%2C-1356%2C2552%2C2935%2C0_0&invitationId=inv_fba2bebb-2d65-4097-894a-9f53bdc07da0" > Link do Diagrama de classes (Lucidchart) </a>
 
 ## Requisitos Funcionais

***Gestão de Funcionários***


| Código   | Descrição                                                                           | Prioridade | Dependências |
| -------- | ----------------------------------------------------------------------------------- | ---------- | ------------ |
| **RF01** | Cadastrar funcionários, incluindo: nome completo, ID, idade, gênero, setor e cargo. | **Must**   | Nenhuma      |
| **RF02** | Consultar a lista de funcionários por setor.                                        | **Should** | RF01         |
| **RF03** | Atualizar dados dos funcionários (nome, idade, gênero, cargo e setor).              | **Must**   | RF01         |
| **RF04** | Excluir funcionários do cadastro (em caso de demissão ou desligamento).             | **Must**   | RF01         |
| **RF05** | Visualizar o total de funcionários por setor.                                       | **Should** | RF01, RF02   |

***Gestão de Salários e Benefícios***


| Código   | Descrição                                                                                                                                                                                                                                                            | Prioridade | Dependências |
| -------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------- | ------------ |
| **RF06** | Calcular automaticamente o salário bruto e líquido de cada funcionário, considerando impostos, vale transporte, vale refeição (R\$300, podendo variar conforme cargo), plano de saúde (mínimo R\$3000, podendo variar), plano odontológico (R\$3000) e bonificações. | **Must**   | RF01, RF07   |
| **RF07** | Consultar e atualizar os valores dos benefícios, impostos e bonificações.                                                                                                                                                                                            | **Must**   | Nenhuma      |
| **RF08** | Emitir demonstrativo salarial dos funcionários.                                                                                                                                                                                                                      | **Should** | RF06         |

***Gestão de Produtos e Estoque***


| Código   | Descrição                                                                                            | Prioridade | Dependências |
| -------- | ---------------------------------------------------------------------------------------------------- | ---------- | ------------ |
| **RF09** | Cadastrar produtos, contendo: nome, código, preço de compra, preço de venda e quantidade em estoque. | **Must**   | Nenhuma      |
| **RF10** | Consultar a lista de produtos cadastrados.                                                           | **Should** | RF09         |
| **RF11** | Atualizar informações dos produtos (preço, quantidade, descrição).                                   | **Must**   | RF09         |
| **RF12** | Controlar entradas (reposição) e saídas (vendas) de produtos no estoque.                             | **Must**   | RF09, RF11   |
| **RF13** | Verificar disponibilidade de produtos para venda.                                                    | **Must**   | RF09, RF12   |

***Gestão Financeira e Caixa***


| Código   | Descrição                                                                     | Prioridade | Dependências           |
| -------- | ----------------------------------------------------------------------------- | ---------- | ---------------------- |
| **RF14** | Registrar entradas de caixa (vendas, investimentos, etc.).                    | **Must**   | RF09, RF12, RF23       |
| **RF15** | Registrar saídas de caixa (salários, compras, despesas gerais).               | **Must**   | RF06, RF09, RF12, RF23 |
| **RF16** | Visualizar o saldo atual do caixa.                                            | **Must**   | RF14, RF15             |
| **RF17** | Gerar uma estimativa de lucro mensal e anual com base nas vendas programadas. | **Should** | RF14, RF15, RF23       |
| **RF18** | Gerar relatório financeiro (entradas, saídas e saldo).                        | **Should** | RF14, RF15, RF16       |

***Gestão de Transportadoras***


| Código   | Descrição                                                                                                         | Prioridade | Dependências |
| -------- | ----------------------------------------------------------------------------------------------------------------- | ---------- | ------------ |
| **RF19** | Cadastrar transportadoras com os seguintes dados: nome, ID e áreas de cobertura (municípios, regiões ou estados). | **Must**   | Nenhuma      |
| **RF20** | Consultar a lista de transportadoras e suas áreas de atendimento.                                                 | **Could**  | RF19         |
| **RF21** | Atualizar os dados das transportadoras (nome, áreas de atendimento).                                              | **Must**   | RF19         |
| **RF22** | Visualizar quantidade total de transportadoras parceiras.                                                         | **Could**  | RF19, RF20   |

***Gestão de Negócios em Andamento***


| Código   | Descrição                                                                                                                                                                        | Prioridade | Dependências     |
| -------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------- | ---------------- |
| **RF23** | Registrar negócios em andamento, identificando tipo (venda ou compra), produtos envolvidos, quantidade e funcionários envolvidos (vendedores ou responsáveis pelo almoxarifado). | **Must**   | RF01, RF09, RF13 |
| **RF24** | Consultar todos os negócios em andamento.                                                                                                                                        | **Should** | RF23             |
| **RF25** | Atualizar o status de um negócio (em andamento, concluído ou cancelado).                                                                                                         | **Must**   | RF23             |
