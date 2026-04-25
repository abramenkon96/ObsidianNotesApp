package com.nikola.obsidiannotes.model

import java.util.Date

data class Note(
    val fileName: String,      // Имя файла на диске
    val title: String,         // Заголовок из YAML
    val group: String,         // Группа из YAML
    val color: String,         // Цвет (Hex string, например #FF0000)
    val content: String,       // Текст заметки (Markdown)
    val isPinned: Boolean,     // Статус закрепления в уведомлении
    val isArchived: Boolean,   // Находится ли в архиве
    val lastModified: Long     // Дата изменения
) {
    // Метод для генерации YAML Frontmatter при сохранении
    fun toFileContent(): String {
        val yaml = """
            ---
            title: "$title"
            group: "$group"
            color: "$color"
            pinned: $isPinned
            status: "${if (isArchived) "archived" else "active"}"
            ---
            $content
        """.trimIndent()
        return yaml
    }
}
