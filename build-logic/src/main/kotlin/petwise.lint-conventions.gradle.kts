plugins {
    id("com.diffplug.spotless")
}

spotless {
    java {
        googleJavaFormat()
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        trimTrailingWhitespace()
        endWithNewline()
    }
}
