# Log Analyzer

![Java](https://img.shields.io/badge/Java-FF7900?style=for-the-badge&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![CLI](https://img.shields.io/badge/Command_Line_Interface-231F20?style=for-the-badge&logo=gnome-terminal&logoColor=white)

---

## About The Project

Log Analyzer is a command-line interface tool built with Java for analyzing log files directly from your terminal. It allows you to filter log content by level, keywords, and date, displaying results in a structured format and exporting the output to a text file automatically.

---

## Features

- Interactive CLI with built-in commands (`help`, `clear`, `exit`)
- Log file analysis with total line and character count
- Filter by log level (e.g. `ERROR`, `WARN`, `INFO`)
- Filter by one or more keywords with word list support
- Filter by date with customizable date format
- Automatic export of filtered results to a `.txt` file in the `logs/` directory

---

## Installation

### Requirements

- Java 21+
- Maven 3.8+

### Steps

**1. Clone the repository**

```bash
git clone https://github.com/RenanTheFato/log-analyzer-cli.git
cd log-analyzer-cli
```

**2. Build the project**

```bash
mvn package
```

**3. Run the application**

```bash
java -jar target/cli-${VERSION_NAME}.jar
```

---

## Usage

Once the CLI is running, use the `log` command with the desired options:

```bash
# Analyze a file
log -f path/to/file.log

# Filter by log level
log -f path/to/file.log -l ERROR

# Filter by keyword
log -f path/to/file.log -wl null

# Filter by multiple keywords
log -f path/to/file.log -wl timeout,connection,refused

# Filter by date (requires both -af and -df)
log -f path/to/file.log -af 31/12/1999 -df dd/MM/yyyy

# Combine filters
log -f path/to/file.log -l ERROR -wl timeout -af 31/12/1999 -df dd/MM/yyyy
```

### Available Options

| Option | Description |
|---|---|
| `-f`, `--file` | Path to the log file (**required**) |
| `-l`, `--level` | Filter rows by log level |
| `-wl`, `--wordlist` | Keywords to filter rows (comma-separated) |
| `-af`, `--after` | Show logs from this date onwards |
| `-df`, `--dateformat` | Date format used in the log file |

### Built-in Commands

| Command | Description |
|---|---|
| `help` | Show available commands |
| `clear` / `cls` | Clear the terminal |
| `exit` | Exit the application |

---

## Project Structure

```
log-analyzer-cli/
├── logs/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── loganalyzercli/
│   │   │           ├── commands/
│   │   │           ├── handlers/
│   │   │           └── utils/
│   │   └── resources/
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

---

## Contact

Renan - [GitHub](https://github.com/RenanTheFato)

Renan - [LinkedIn](https://www.linkedin.com/in/renan-santana007)

Email - renan.santana007@hotmail.com

Project Link: [https://github.com/RenanTheFato/log-analyzer-cli](https://github.com/RenanTheFato/log-analyzer-cli)