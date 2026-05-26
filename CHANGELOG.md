# Kotlin Data Class to JSON Changelog

All notable changes to the "Kotlin Data Class to JSON" plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Custom data generation templates
- Export to file functionality
- Batch processing for multiple data classes

## [1.0.1] - 2025-10-16

### Added
- Support for updated Gemini AI models:
  - `gemini-2.5-flash` (recommended)
  - `gemini-2.5-pro`
  - `gemini-2.5-flash-lite-preview-06-17`
  - `learnlm-2.0-flash-experimental`
  - `gemini-2.0-flash-lite`
  - `gemma-3-1b-it`

### Changed
- Extend Gemini HTTP request timeouts to 5 minutes to support long-running requests
- Increase Gemini GenerationConfig maxOutputTokens to 8192 for larger data class outputs
- Bump IntelliJ Platform to 2025.1 and update since-build to 251 for K2-by-default IDEs
- Declare Kotlin K2 compatibility in plugin.xml
- Improve thread-safety by wrapping PSI reads in ReadAction
- Simplify JSON generation by avoiding heavy resolve and using type text fallback
- Bump plugin version to 1.0.1
- Show a warning notification and do not copy when Gemini HTTP response is non-200

## [1.0.0] - 2024-12-17

### Added
- 🚀 **Initial Release** - First stable version of Kotlin Data Class to JSON plugin
- 🎯 **One-click JSON generation** from Kotlin data classes via context menu
- ⌨️ **Keyboard shortcut support** (`Ctrl+Alt+J`) for quick access
- 🤖 **AI-powered realistic data generation** using Google Gemini API
- 🌏 **Indonesian locale support** for culturally relevant sample data
- 📋 **Automatic clipboard integration** - generated JSON is copied automatically
- 🔄 **Support for nested data classes** and complex object hierarchies
- 📊 **Comprehensive type support**:
  - Primitive types (String, Int, Boolean, Double, Float, Long)
  - Collections (List, Set, Map)
  - Nullable types with proper null handling
  - Enum classes with first value selection
  - Recursive data class generation
- 🎨 **Multiple generation modes**:
  - Basic generation with simple sample data
  - AI-powered generation with realistic Indonesian data
- 🔧 **Google Gemini API integration**:
  - Support for multiple models (gemini-1.5-flash, gemini-1.5-pro, gemini-1.0-pro, gemma-3-1b-it)
  - Secure API key management
  - Error handling and fallback mechanisms
- 🎛️ **User-friendly interface**:
  - Model selection dialog
  - Progress indicators for AI generation
  - Informative notifications
  - Context-aware action availability
- 📝 **JSON formatting** with proper indentation and structure
- 🔒 **Security features**:
  - Local API key storage
  - No codebase data sent to external services (only data class structure)
- 🏗️ **Technical features**:
  - Background processing to avoid UI blocking
  - Kotlin PSI analysis for accurate type detection
  - Comprehensive error handling and user feedback
  - IntelliJ Platform 2024.2+ compatibility

### Technical Details
- **Minimum IDE Version**: IntelliJ IDEA 2024.2 (build 242)
- **Maximum IDE Version**: IntelliJ IDEA 2025.2.* (build 252.*)
- **Dependencies**:
  - IntelliJ Platform
  - Kotlin Plugin
  - Ktor Client for HTTP requests
  - Kotlinx Serialization for JSON handling
- **Supported Languages**: Kotlin data classes
- **Platform**: All IntelliJ-based IDEs with Kotlin support

---

Getting Started

This is the initial release of the Kotlin Data Class to JSON plugin. To get started:

1. **Install the plugin** from JetBrains Marketplace or manually
2. **Open a Kotlin file** with data classes
3. **Right-click on a data class** and select "Generate JSON Sample"
4. **Optionally configure Google Gemini API** for realistic data generation

Known Issues

- None reported for initial release

Future Plans

- **Enhanced AI Models**: Support for more AI providers and models
- **Custom Templates**: User-defined data generation templates
- **Batch Processing**: Generate JSON for multiple data classes at once
- **Export Options**: Save generated JSON to files
- **Configuration UI**: Settings panel for customization
- **More Locales**: Support for additional cultural data beyond Indonesian

Support & Feedback

- **Bug Reports**: [GitHub Issues](https://github.com/bayutb123/kdctojson/issues)
- **Feature Requests**: [GitHub Issues](https://github.com/bayutb123/kdctojson/issues)
- **Email**: bayutantra28@gmail.com

[Unreleased]: https://github.com/bayutb123/kdctojson/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/bayutb123/kdctojson/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/bayutb123/kdctojson/releases/tag/v1.0.0
