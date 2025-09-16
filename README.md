# Kotlin Data Class to JSON

[![Build](https://github.com/bayutb123/kdctojson/workflows/Build/badge.svg)](https://github.com/bayutb123/kdctojson/actions)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.bayutb123.kdctojson.svg)](https://plugins.jetbrains.com/plugin/com.github.bayutb123.kdctojson)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.bayutb123.kdctojson.svg)](https://plugins.jetbrains.com/plugin/com.github.bayutb123.kdctojson)

<!-- Plugin description -->
**Kotlin Data Class to JSON** is an IntelliJ IDEA plugin that generates realistic JSON samples from Kotlin data classes with AI-powered content generation.

Transform your Kotlin data classes into realistic JSON samples instantly! This plugin supports both basic JSON generation and AI-powered realistic data generation using Google Gemini API, with special support for Indonesian locale-aware sample data.

**Key Features:**
- 🚀 **One-click JSON generation** from any Kotlin data class
- 🤖 **AI-powered realistic data** using Google Gemini API
- 🌏 **Indonesian locale support** for culturally relevant sample data
- 📋 **Automatic clipboard integration** - generated JSON is copied automatically
- 🔄 **Support for nested data classes** and collections
- ⌨️ **Keyboard shortcut** support (`Ctrl+Alt+J`)
- 🎯 **Context menu integration** - right-click any data class
<!-- Plugin description end -->

## 📖 Table of Contents

- [Installation](#-installation)
- [Usage](#-usage)
  - [Basic Usage](#basic-usage)
  - [AI-Powered Generation](#ai-powered-generation)
  - [Keyboard Shortcuts](#keyboard-shortcuts)
- [Configuration](#-configuration)
  - [Google Gemini API Setup](#google-gemini-api-setup)
- [Examples](#-examples)
- [Supported Types](#-supported-types)
- [Development](#-development)
  - [Building from Source](#building-from-source)
  - [Running Tests](#running-tests)
- [Contributing](#-contributing)
- [Changelog](#-changelog)
- [License](#-license)


## 📦 Installation

### From JetBrains Marketplace (Recommended)

1. Open IntelliJ IDEA
2. Go to `File` → `Settings` → `Plugins`
3. Search for "Kotlin Data Class to JSON"
4. Click `Install` and restart the IDE

### Manual Installation

1. Download the latest release from the [Releases page](https://github.com/bayutb123/kdctojson/releases)
2. Go to `File` → `Settings` → `Plugins`
3. Click the gear icon and select `Install Plugin from Disk...`
4. Select the downloaded `.zip` file
5. Restart IntelliJ IDEA

### Requirements

- IntelliJ IDEA 2024.2 or later (up to 2025.2.*)
- Kotlin plugin enabled
- (Optional) Google Gemini API key for AI-powered generation

## 🚀 Usage

### Basic Usage

1. **Open a Kotlin file** containing a data class
2. **Place your cursor** inside the data class
3. **Right-click** and select `Generate JSON Sample` from the context menu

   *OR*

   **Use the keyboard shortcut** `Ctrl+Alt+J`

4. **Choose generation mode:**
   - **Basic Generation**: Creates simple sample data
   - **AI-Powered Generation**: Uses Google Gemini for realistic data (requires API key)

5. **Generated JSON is automatically copied** to your clipboard!

### AI-Powered Generation

For more realistic and diverse sample data, you can use the AI-powered generation feature:

1. Follow the basic usage steps above
2. When prompted, select an AI model (e.g., `gemini-1.5-flash`)
3. If it's your first time, you'll be prompted to enter your Google Gemini API key
4. The plugin will generate realistic, Indonesian locale-aware sample data

### Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl+Alt+J` | Generate JSON Sample |


## ⚙️ Configuration

### Google Gemini API Setup

To use the AI-powered JSON generation feature, you'll need a Google Gemini API key:

1. **Get an API Key:**
   - Visit the [Google AI Studio](https://makersuite.google.com/app/apikey)
   - Sign in with your Google account
   - Create a new API key

2. **Configure in the Plugin:**
   - The first time you use AI generation, the plugin will prompt you for the API key
   - Enter your API key when prompted
   - The key will be securely stored for future use

3. **Supported Models:**
   - `gemini-2.5-flash` (recommended)
   - `gemini-2.5-pro`
   - `gemini-2.5-flash-lite-preview-06-17`
   - `learnlm-2.0-flash-experimental`
   - `gemini-2.0-flash-lite`
   - `gemma-3-1b-it`

> [!NOTE]
> The API key is stored locally and securely. The plugin only sends your data class structure to Google's API, not your entire codebase.

## 📋 Examples

### Basic Data Class

```kotlin
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val isActive: Boolean
)
```

**Generated JSON (Basic):**
```json
{
    "id": 0,
    "name": "example",
    "email": "example",
    "isActive": true
}
```

**Generated JSON (AI-Powered):**
```json
{
    "id": 12345,
    "name": "Budi Santoso",
    "email": "budi.santoso@gmail.com",
    "isActive": true
}
```

### Complex Data Class with Nested Objects

```kotlin
data class Address(
    val street: String,
    val city: String,
    val postalCode: String
)

data class Person(
    val name: String,
    val age: Int,
    val addresses: List<Address>,
    val preferences: Map<String, String>
)
```

**Generated JSON (AI-Powered):**
```json
{
    "name": "Sari Dewi",
    "age": 28,
    "addresses": [
        {
            "street": "Jl. Sudirman No. 123",
            "city": "Jakarta",
            "postalCode": "10220"
        },
        {
            "street": "Jl. Malioboro No. 45",
            "city": "Yogyakarta",
            "postalCode": "55271"
        }
    ],
    "preferences": {
        "language": "Indonesian",
        "theme": "dark",
        "notifications": "enabled"
    }
}
```

## 🔧 Supported Types

The plugin supports a wide range of Kotlin types:

### Primitive Types
- `String` → `"example"` or realistic names/text
- `Int`, `Long` → `0` or realistic numbers
- `Boolean` → `true`/`false`
- `Double`, `Float` → `0.0` or realistic decimal values

### Collections
- `List<T>` → Array with 3 sample elements
- `Set<T>` → Array with unique elements
- `Map<K, V>` → Object with key-value pairs

### Complex Types
- **Data Classes** → Nested JSON objects (recursive generation)
- **Enums** → First enum value as string
- **Nullable Types** → Proper null handling

### Special Features
- **Indonesian Locale Support**: Names, addresses, and cultural data
- **Realistic Data**: Phone numbers, emails, addresses in Indonesian format
- **Nested Structures**: Unlimited depth for complex object hierarchies

## 🛠️ Development

### Building from Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/bayutb123/kdctojson.git
   cd kdctojson
   ```

2. **Build the plugin:**
   ```bash
   ./gradlew buildPlugin
   ```

3. **Run in development mode:**
   ```bash
   ./gradlew runIde
   ```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew check
```

### Project Structure

```
src/
├── main/kotlin/com/github/bayutb123/kdctojson/
│   ├── GetJsonAction.kt              # Main action handler
│   └── utils/
│       ├── JSONGenerator.kt          # Core JSON generation logic
│       ├── GeminiUtils.kt           # Google Gemini API integration
│       ├── GeminiCredentialManager.kt # API key management
│       ├── NotificationUtils.kt      # User notifications
│       └── JsonUtils.kt             # JSON formatting utilities
└── main/resources/META-INF/
    └── plugin.xml                   # Plugin configuration
```


## Plugin configuration file

The plugin configuration file is a plugin.xml file located in the `src/main/resources/META-INF` directory.
It provides general information about the plugin, its dependencies, extensions, and actions.

```xml
<idea-plugin>
    <id>com.github.bayutb123.kdctojson</id>
    <name>Kotlin Data Class to JSON</name>
    <vendor email="bayutantra28@gmail.com" url="https://github.com/bayutb123">By.U</vendor>

    <!-- Require IntelliJ IDEA 2024.2+ -->
    <idea-version since-build="242" until-build="252.*"/>

    <depends>com.intellij.modules.platform</depends>
    <depends>org.jetbrains.kotlin</depends>

    <extensions defaultExtensionNs="com.intellij">
        <!-- Register a notification group for user feedback -->
        <notificationGroup id="com.github.bayutb123.kdctojson.notifications" 
                          displayType="BALLOON" 
                          bundle="messages.KdcToJsonBundle" 
                          key="notification.group.title"/>
    </extensions>

    <actions>
        <group id="kdctojson.group" text="KDC to JSON" popup="true">
            <action id="com.github.bayutb123.kdctojson.GetJsonAction"
                    class="com.github.bayutb123.kdctojson.GetJsonAction"
                    text="Generate JSON Sample"
                    description="Generate realistic JSON sample from Kotlin data class">
                <keyboard-shortcut keymap="$default" first-keystroke="ctrl alt J"/>
            </action>
            <add-to-group group-id="EditorPopupMenu" anchor="last"/>
            <add-to-group group-id="ProjectViewPopupMenu" anchor="last"/>
        </group>
    </actions>
</idea-plugin>
```

You can read more about this file in the [Plugin Configuration File][docs:plugin.xml] section of our documentation.


## 💻 Sample Code & Implementation

Here are key code examples showing how the plugin works internally:

### Main Action Handler

<code_snippet path="src/main/kotlin/com/github/bayutb123/kdctojson/GetJsonAction.kt" mode="EXCERPT">
````kotlin
class GetJsonAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val dataClass = findDataClassAtCursor(psiFile, editor) ?: run {
            NotificationUtils.showWarning(project, "Please place the cursor inside a Kotlin data class.")
            return
        }

        val model = GeminiUtils.promptModelSelection(project)
        var apiKey = model?.let { GeminiCredentialManager.getInstance().getApiKey() }

        generateAndCopyJson(project, dataClass, model, apiKey)
    }
}
````
</code_snippet>

### JSON Generation Logic

<code_snippet path="src/main/kotlin/com/github/bayutb123/kdctojson/utils/JSONGenerator.kt" mode="EXCERPT">
````kotlin
object JSONGenerator {
    fun generateSampleJson(project: Project, dataClass: KtClass, indentLevel: Int = 0): String {
        val constructorProps = dataClass.primaryConstructor?.valueParameters?.filter { it.hasValOrVar() } ?: emptyList()
        val bodyProps = dataClass.getBody()?.properties ?: emptyList()
        val allProps: List<KtCallableDeclaration> = constructorProps + bodyProps

        val jsonFields = allProps.mapNotNull { prop ->
            val name = prop.name ?: return@mapNotNull null
            val typeReference = prop.typeReference
            val kotlinType = typeReference?.let {
                val bindingContext = it.analyze(BodyResolveMode.FULL)
                bindingContext[BindingContext.TYPE, it]
            }
            val value = generateValueForType(project, kotlinType, indentLevel + 1)
            "\"$name\": $value"
        }.joinToString(",\n")

        return "{\n$jsonFields\n}"
    }
}
````
</code_snippet>

### AI-Powered Generation with Gemini

<code_snippet path="src/main/kotlin/com/github/bayutb123/kdctojson/utils/JSONGenerator.kt" mode="EXCERPT">
````kotlin
suspend fun generateJsonWithGemini(apiKey: String, dataClassText: String, model: String): String {
    val prompt = """
        Generate a realistic, sample JSON object based on the following Kotlin data class.
        The JSON should be populated with plausible, diverse, and realistic data from indonesia.
        Do not include any explanations, comments, or markdown code fences in your response.
        Only output the raw JSON object itself.

        Data Class Definition:
        ```kotlin
        $dataClassText
        ```
    """.trimIndent()

    val result = client.post("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey") {
        contentType(ContentType.Application.Json)
        setBody(/* Gemini API request body */)
    }

    return GeminiUtils.extractTextFromResponse(result.bodyAsText()) ?: "null"
}
````
</code_snippet>

### Type-Specific Value Generation

<code_snippet path="src/main/kotlin/com/github/bayutb123/kdctojson/utils/JSONGenerator.kt" mode="EXCERPT">
````kotlin
private fun generateValueForType(project: Project, kotlinType: KotlinType?, indentLevel: Int): String {
    if (kotlinType == null) return "null"

    val nonNullableType = kotlinType.makeNotNullable()

    return when {
        KotlinBuiltIns.isString(nonNullableType) -> "\"example\""
        KotlinBuiltIns.isInt(nonNullableType) -> "0"
        KotlinBuiltIns.isBoolean(nonNullableType) -> "true"
        KotlinBuiltIns.isListOrNullableList(nonNullableType) -> {
            val genericType = nonNullableType.arguments.firstOrNull()?.type
            val sampleElement = generateValueForType(project, genericType, indentLevel)
            val elements = List(3) { sampleElement }
            "[ " + elements.joinToString(", ") + " ]"
        }
        // Handle data classes recursively
        nonNullableType.constructor.declarationDescriptor is ClassDescriptor -> {
            val classDescriptor = nonNullableType.constructor.declarationDescriptor as ClassDescriptor
            val psiClass = classDescriptor.source.getPsi() as? KtClass

            if (psiClass?.isData() == true) {
                generateSampleJson(project, psiClass, indentLevel)
            } else "null"
        }
        else -> "null"
    }
}
````
</code_snippet>

### Plugin Configuration

<code_snippet path="src/main/resources/META-INF/plugin.xml" mode="EXCERPT">
````xml
<idea-plugin>
    <id>com.github.bayutb123.kdctojson</id>
    <name>Kotlin Data Class to JSON</name>
    <vendor email="bayutantra28@gmail.com" url="https://github.com/bayutb123">By.U</vendor>

    <depends>com.intellij.modules.platform</depends>
    <depends>org.jetbrains.kotlin</depends>

    <actions>
        <group id="kdctojson.group" text="KDC to JSON" popup="true">
            <action id="com.github.bayutb123.kdctojson.GetJsonAction"
                    class="com.github.bayutb123.kdctojson.GetJsonAction"
                    text="Generate JSON Sample"
                    description="Generate realistic JSON sample from Kotlin data class">
                <keyboard-shortcut keymap="$default" first-keystroke="ctrl alt J"/>
            </action>
            <add-to-group group-id="EditorPopupMenu" anchor="last"/>
            <add-to-group group-id="ProjectViewPopupMenu" anchor="last"/>
        </group>
    </actions>
</idea-plugin>
````
</code_snippet>

### Key Implementation Features

- **🎯 Context-Aware Detection**: Automatically detects Kotlin data classes at cursor position
- **🔄 Recursive Generation**: Handles nested data classes and complex object hierarchies
- **🤖 AI Integration**: Seamless integration with Google Gemini API for realistic data
- **📋 Clipboard Integration**: Automatically copies generated JSON to clipboard
- **⚡ Background Processing**: Non-blocking UI with progress indicators
- **🔧 Type Safety**: Comprehensive Kotlin type system support
- **🌐 Locale Support**: Indonesian-specific realistic data generation

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

### Reporting Issues

1. Check existing [issues](https://github.com/bayutb123/kdctojson/issues) first
2. Create a new issue with:
   - Clear description of the problem
   - Steps to reproduce
   - Expected vs actual behavior
   - Your environment details (IDE version, plugin version)

### Contributing Code

1. **Fork the repository**
2. **Create a feature branch:**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes** and add tests if applicable
4. **Run tests:**
   ```bash
   ./gradlew test
   ```
5. **Commit your changes:**
   ```bash
   git commit -m "Add amazing feature"
   ```
6. **Push to your branch:**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

### Development Guidelines

- Follow Kotlin coding conventions
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed history of changes.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [JetBrains](https://www.jetbrains.com/) for the IntelliJ Platform
- [Google](https://ai.google.dev/) for the Gemini API
- [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template) for the project structure

## 📞 Support

- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/bayutb123/kdctojson/issues)
- 💡 **Feature Requests**: [GitHub Issues](https://github.com/bayutb123/kdctojson/issues)
- 📧 **Email**: bayutantra28@gmail.com

---

**Made with ❤️ by [By.U](https://github.com/bayutb123)**

*If you find this plugin helpful, please consider giving it a ⭐ on GitHub!*
