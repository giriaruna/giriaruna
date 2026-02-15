# Web Crawling Engine

## Overview

This project is a high-performance backend system designed to **crawl, categorize, and normalize web assets**.  
It leverages **concurrent processing** and **Breadth-First Search (BFS)** traversal to efficiently map the image landscape of a target domain.

---

## Getting Started

### Prerequisites

- **Java 8**
- **Apache Maven**

### Run the Development Server

From the project root directory, run:

```bash
mvn clean package jetty:run -DskipTests
```

Then open your browser and navigate to:

```
http://localhost:8080
```

---

## Technical Architecture

The system follows a **modular, object-oriented design** to ensure maintainability and strict separation of concerns.

### Core Components

#### `ImageFinder.java`
**Controller Layer**  
Handles incoming HTTP POST requests and manages the lifecycle of the crawl response.

#### `CrawlerEngine.java`
**Service Layer**  
Manages task distribution to the worker pool and coordinates traversal logic.

#### `UrlDepth.java`
**Data Model**  
Encapsulates the state of each individual crawl task.

#### `CrawlRes.java`
**Result Object**  
Stores categorized assets using thread-safe concurrent collections.

---

## Core Features

### Multi-Threaded Execution

- Fixed thread pool of **10 concurrent workers**
- Maximizes network I/O throughput
- Minimizes crawl latency

### Breadth-First Search (BFS)

- Level-order traversal strategy
- Systematic exploration of the domain frontier
- Prevents deep-first traversal bias

### Automated Categorization

Includes heuristics to identify:

- Brand assets (logos, favicons)
- Human subjects through metadata analysis

### URL Normalization

- Strips query parameters
- Removes URL fragments
- Prevents redundant processing
- Eliminates duplicate entries

---

## Project Cleanup

Before packaging the project, run:

```bash
mvn clean
```

This removes compiled binaries and clears the `target/` directory.

---

## Testing

All test URLs must be documented in:

```
test-links.txt
```

This file should be located in the project root directory.
