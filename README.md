# Chess Android

Aplicativo Android nativo em Kotlin + Jetpack Compose.

## Estado da versão 0.1.0
- Interface escura minimalista.
- Tabuleiro 8×8 na posição inicial.
- Seleção de peças e movimentação visual.
- Reiniciar e inverter o tabuleiro.
- GitHub Actions configurado para gerar e publicar o APK de debug como artefato.

**Importante:** esta primeira base ainda não valida as regras oficiais do xadrez e não inclui Stockfish. A integração do motor e a validação legal dos lances são as próximas etapas.

## Build
O workflow roda ao enviar commits para `main` ou manualmente em **Actions → Android APK → Run workflow**.
