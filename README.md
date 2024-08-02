<p align="center">
  <img src="logo/meow.png" alt="Meow Logo" style="max-width: 100%; height: auto;">
</p>

# Meow: JSON Pretty Printer

A simple CLI tool to pretty print your JSON.

<div style="text-align: center;">
  <img src="img/pretty.png" alt="Pretty JSON" style="max-width: 100%; height: auto;">
  <img src="img/not-pretty.png" alt="Not Pretty JSON" style="max-width: 100%; height: auto; margin-bottom: 10px;">
</div>

## Features

- **Pretty Printing**: Meow automatically formats JSON data with proper indentation and line breaks, ensuring that the output is clean and well-organized. This makes it easier to read and understand complex JSON structures.

- **Color-Coded Output**: To further enhance readability, Meow highlights different JSON data types with distinct colors:

  - **Strings**: ![#00FF00](https://via.placeholder.com/15/00FF00/000000?text=+) Green
  - **Booleans**: ![#FFFF00](https://via.placeholder.com/15/FFFF00/000000?text=+) Yellow
  - **Numbers**: ![#FF00FF](https://via.placeholder.com/15/FF00FF/000000?text=+) Magenta
  - **Map Keys**: ![#00FFFF](https://via.placeholder.com/15/00FFFF/000000?text=+) Cyan

- **CLI Integration**: Meow seamlessly integrates with the command line, supporting input redirection and pipes. This allows users to easily format JSON data from files, APIs, or other command-line tools.

## Requirements

- **Java 17 or higher**

### Installation

To install Meow, follow these simple steps:

1. Download the attached 'jar' file and run:
   ```bash
   > echo '{"=-..-=: "MEOW"}' | java -jar meow
   ```

2. You can also create a bash/batch script to run it in a more simple way.
   Create a meow.sh file:
   ```bash
      #!/bin/bash
      java -jar meow.jar "@$" 
   ```
   and now you can run: 
   ```bash
    > echo '{"=-..-=: "MEOW"}' | meow
   ```
