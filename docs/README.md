# Karate Chop Debugger

Welcome to the Karate Chop Debugger! This unofficial IntelliJ plugin makes debugging Karate tests a whole lot easier by bringing powerful, native debugging features directly to your `.feature` files.

## How It Works

The plugin integrates with IntelliJ's debugging UI to provide a seamless experience. When you start a debug session, the plugin launches the Karate test runner and attaches a custom `RuntimeHook`. This hook communicates with the IDE, allowing you to control execution, inspect variables, and evaluate expressions on the fly.

### Core Components

1.  **IntelliJ Plugin:** The core of the debugger, providing the UI, breakpoint management, and communication with the debug agent.
2.  **Debug Hook:** A custom implementation of Karate's `RuntimeHook` interface that intercepts test execution at each step. It uses a `CountDownLatch` to pause and resume the execution thread, allowing for interactive debugging.
3.  **Communication Bridge:** A simple TCP-based communication protocol that allows the IDE and the debug hook to exchange information, such as breakpoint locations, variable values, and debugger commands (step, resume, etc.).

## Key Features

*   **Native Breakpoints:** Add breakpoints to your `.feature` files just like you would in any other language, using the standard IntelliJ shortcuts (`Ctrl+F8` / `Cmd+F8`).
*   **Variable Inspection:** When a breakpoint is hit, the "Variables" panel shows you all the current Karate variables and their values.
*   **Expression Evaluation:** The evaluation field allows you to run Karate expressions or JavaScript code in the current context. Prefix your expression with `*` to run a Karate expression; otherwise, it will be treated as JavaScript.
*   **Step-Through Execution:** Use the standard "Step Over" and "Resume" buttons to control the execution flow.
*   **Syntax Highlighting:** The plugin provides custom syntax highlighting for `.feature` files to improve readability.

---

## Presentation (Marp/Marpit)

The deck lives at `docs/presentation.md` and is compatible with Marp (marp-cli).

Build to HTML or PDF:

```bash
# Install marp-cli (Node.js)
npm i -g @marp-team/marp-cli

# HTML
marp docs/presentation.md -o docs/presentation.html

# PDF (requires Chrome / Chromium available in PATH)
marp docs/presentation.md -o docs/presentation.pdf
```

Live preview while editing:

```bash
marp -w docs/presentation.md -o docs/presentation.html
```

Tips:
- If fonts or emojis render oddly in PDF, try `--allow-local-files` and ensure Chromium is installed.
- You can also use the Marp VS Code extension for a live preview.

### Custom theme

A Monokai theme is provided at `docs/theme.css`. Use it with marp-cli:

```bash
# HTML with theme
marp --theme-set docs/theme.css --theme monokai docs/presentation.md -o docs/presentation.html

# PDF with theme
marp --theme-set docs/theme.css --theme monokai docs/presentation.md -o docs/presentation.pdf

# Watch mode with theme
marp -w --theme-set docs/theme.css --theme monokai docs/presentation.md -o docs/presentation.html
```
