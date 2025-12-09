# VEND - Sistema de Vendas de Carros

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design" />
</div>

## 📱 Sobre o Projeto

VEND é um aplicativo Android nativo para vendas de carros, desenvolvido em Java. O sistema oferece uma interface intuitiva para visualização de catálogo de veículos, com funcionalidades completas de autenticação e gerenciamento de usuários.

## ✨ Funcionalidades

- 🔐 **Autenticação de Usuários**
  - Login seguro com validação de email e senha
  - Cadastro de novos usuários
  - Validações em tempo real

- 🚗 **Catálogo de Veículos**
  - Visualização em grid responsivo
  - Exibição de imagens dos carros
  - Informações detalhadas (marca, modelo, ano, carroceria, preço)
  - Formatação monetária em Real (BRL)

- 🎨 **Interface Moderna**
  - Design baseado em Material Design
  - Animações de transição suaves
  - Feedback visual para ações do usuário
  - Loading states e tratamento de erros

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
com.example.vend/
├── dto/                    # Data Transfer Objects
│   ├── UsuarioClienteCadastrarDTO
│   ├── UsuarioClienteLoginDTO
│   └── UsuarioClienteResponseDTO
├── model/                  # Modelos de dados
│   ├── Carro
│   └── UsuarioCliente
├── network/                # Camada de rede
│   ├── ApiClient
│   ├── ByteArrayTypeAdapter
│   ├── CarroApiService
│   └── UsuarioClienteApiService
└── view/                   # Camada de apresentação
    ├── LoginActivity
    ├── CadastroActivity
    └── CatalogoActivity
```

## 🛠️ Tecnologias Utilizadas

### Core
- **Linguagem:** Java 11
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Build Tool:** Gradle 8.13

### Bibliotecas Principais

| Biblioteca | Versão | Uso |
|-----------|--------|-----|
| **Retrofit** | 3.0.0 | Requisições HTTP REST |
| **Gson** | 3.0.0 | Serialização JSON |
| **OkHttp Logging** | 5.3.2 | Logging de requisições |
| **Room** | 2.6.1 | Persistência local |
| **Material Components** | 1.10.0 | Componentes UI |
| **CardView** | 1.0.0 | Cards customizados |

## 🚀 Como Executar

### Pré-requisitos

- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 11 ou superior
- Android SDK com API Level 36
- Dispositivo/Emulador Android

### Passos para Instalação

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/vend.git
cd vend
```

2. **Abra o projeto no Android Studio**
```bash
File > Open > Selecione a pasta do projeto
```

3. **Sincronize as dependências**
```bash
File > Sync Project with Gradle Files
```

4. **Configure o servidor backend**
   - O app está configurado para usar: `https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/`
   - Para alterar, edite a constante `BASE_URL` nas Activities

5. **Execute o aplicativo**
   - Pressione `Shift + F10` ou clique em "Run"
   - Selecione um dispositivo/emulador

## 📡 API Integration

O aplicativo consome uma API REST com os seguintes endpoints:

### Autenticação
```
POST /usuariosCliente/cadastro
POST /usuariosCliente/login
```

### Carros
```
GET  /carros
GET  /carros/{id}
GET  /carros/marca/{marca}
GET  /carros/marca/{marca}/modelo/{modelo}
POST /carros
DELETE /carros/{id}
```

## 🎨 Design System

### Cores Principais
- **Primary:** `#0084FF` (Azul)
- **Secondary:** `#6CCBFF` (Azul claro)
- **Background:** `#F5F5F5` (Cinza claro)
- **Surface:** `#FFFFFF` (Branco)

### Componentes Customizados
- Inputs arredondados com fundo branco
- Botões com bordas arredondadas (10dp)
- Cards com elevação e sombras
- Grid de 2 colunas para catálogo

## 📦 Estrutura de Dados

### Modelo Carro
```java
{
  "id": Long,
  "modelo": String,
  "marca": String,
  "ano": Integer,
  "preco": Double,
  "carroceria": String,
  "imagem": byte[]  // Base64 encoded
}
```

### Modelo UsuarioCliente
```java
{
  "id": Integer,
  "email": String,
  "senha": String
}
```

## 🔒 Segurança

- Senhas com validação mínima de 6 caracteres
- Validação de formato de email
- Comunicação HTTPS com o backend
- Timeout configurado (30s)
- Retry automático em falhas de conexão

## 🐛 Tratamento de Erros

O aplicativo implementa tratamento robusto de erros:

- **Validação de campos:** Feedback imediato em campos inválidos
- **Erros de rede:** Mensagens amigáveis para problemas de conexão
- **Erros HTTP:** Tratamento específico por código (400, 404, 500)
- **Estados vazios:** Mensagens quando não há dados disponíveis
- **Loading states:** Indicadores visuais durante operações assíncronas

## 🧪 Testes

```bash
# Testes unitários
./gradlew test

# Testes instrumentados (requer dispositivo/emulador)
./gradlew connectedAndroidTest
```

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Contato

Para dúvidas ou sugestões, entre em contato através do email: contato@vend.com

---

<div align="center">
  Desenvolvido com ❤️ usando Android
</div>
