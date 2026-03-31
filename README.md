# Sistema de Produção e Comercialização de Veículos
Este projeto é uma atividade acadêmica que tem como objetivo desenvolver um sistema em Java para simular uma cadeia de produção e comercialização de veículos.

O sistema é composto por três entidades principais: fábrica, lojas e clientes, que operam de forma concorrente e distribuída.

A fábrica é responsável pela produção dos veículos, utilizando múltiplas estações de trabalho com funcionários que compartilham recursos (ferramentas), exigindo controle de sincronização. Os veículos produzidos são armazenados em esteiras com capacidade limitada.

As lojas solicitam veículos à fábrica por meio de comunicação remota (modelo cliente-servidor), armazenam os veículos recebidos e os disponibilizam para venda.

Os clientes, representados por threads, realizam compras de forma aleatória nas lojas, podendo aguardar caso não haja veículos disponíveis.

O sistema deve garantir o controle de concorrência utilizando exclusivamente semáforos, evitando problemas como deadlock e starvation, além de manter a integridade dos dados durante toda a execução.

Também são gerados logs que registram as etapas de produção e comercialização dos veículos ao longo do sistema.
