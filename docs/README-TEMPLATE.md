[![JetBrains Research](https://jb.gg/badges/research.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)

# Tables.kt

Tables.kt is a lightweight Kotlin-Multiplatform library to work with tables of any origin. It is **not** intended as an alternative to [DataFrame](https://github.com/Kotlin/dataframe) library. On the contrary, you can use it together with the [provided module](tables-lt-dataframe). The aim of this library to provide an API for a various tables and row lists.

Another aim is to provide integration between different types of tables. For example load database table with Exposed and convert it to a DataFrame.

The performance could vary depending on the type of the table. For example row-based access to column-based table could be slow and vise-versa. Though in principle, the implementations could be tweaked to be very fast.

The library is intended as multiplatform. It supports JVM, JS-IR and Native targets.

## Installation

${artifact}

## Features

${features}

## Modules

${modules}