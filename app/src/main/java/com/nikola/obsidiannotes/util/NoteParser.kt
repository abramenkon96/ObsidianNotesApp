package com.nikola.obsidiannotes.util

import com.nikola.obsidiannotes.model.Note
import org.yaml.snakeyaml.Yaml
import java.io.File

object NoteParser {
    private val yamlParser = Yaml()
    private val frontmatterRegex = Regex("""^---\s*\n(.*?)\n---\s*\n(.*)""", RegexOption.DOT_MATCHES_ALL)

    fun parse(fileName: String, fullText: String, lastModified: Long): Note {
        val matchResult = frontmatterRegex.find(fullText)
        
        if (matchResult != null) {
            val yamlText = matchResult.groups[1]?.value ?: ""
            val contentText = matchResult.groups[2]?.value ?: ""
            
            val metadata = yamlParser.load<Map<String, Any>>(yamlText) ?: emptyMap()
            
            return Note(
                fileName = fileName,
                title = metadata["title"]?.toString() ?: fileName.removeSuffix(".md"),
                group = metadata["group"]?.toString() ?: "Default",
                color = metadata["color"]?.toString() ?: "#FFFFFF",
                content = contentText.trim(),
                isPinned = metadata["pinned"] as? Boolean ?: false,
                isArchived = (metadata["status"]?.toString() ?: "active") == "archived",
                lastModified = lastModified
            )
        } else {
            // Если метаданных нет, создаем дефолтную заметку
            return Note(
                fileName = fileName,
                title = fileName.removeSuffix(".md"),
                group = "Default",
                color = "#FFFFFF",
                content = fullText.trim(),
                isPinned = false,
                isArchived = false,
                lastModified = lastModified
            )
        }
    }
}
