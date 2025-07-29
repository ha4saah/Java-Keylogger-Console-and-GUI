# 🛠️ Java Keylogger Project

> **by [@ha4saah](https://github.com/ha4saah)**  
> 🔐 *For educational and ethical cybersecurity practice only.*

This repository contains two keylogger implementations in **Java**:  
a **console-based version** and a **GUI-based version** — both designed to capture and log keystrokes along with timestamps.

---

## ⚠️ Disclaimer

**This project is for educational purposes only.**  
Do **not** use keyloggers on any device or network without **explicit written permission**. Unauthorized use of surveillance software is illegal and unethical. Always follow cybersecurity laws and best practices.

---

## 📂 Files in This Repo

| File Name            | Description                          |
|----------------------|--------------------------------------|
| `main.java`          | Console-based Java keylogger         |
| `KeyloggerGUI.java`  | GUI-based Java keylogger             |
| `keylog.txt`         | Output log from console version      |
| `keylog_gui.txt`     | Output log from GUI version          |

---

## ☕ Java Keylogger Overview

### ➤ Console Version (`main.java`)

**Features:**
- Captures key presses and typed characters
- Includes timestamps for each entry
- Uses a tiny, nearly invisible frame to capture input
- Writes output to `keylog.txt`

**How to Run:**
```bash
javac main.java
java main
# Use Ctrl+C to stop
