# Indexing
Необходимо создать библиотеку, реализующую простейший сервис индексации текстовых файлов по словам.
Интерфейс библиотеки должен позволять добавлять в систему каталоги и файлы и выдавать список файлов содержащих заданное слово. Библиотека должна поддерживать многопоточный доступ к индексу, а также отслеживать изменения файлов и состава каталогов на диске. Библиотека должна быть расширяемой по механизму разделения по словам: простое текстовое разбиение, лексеры, etc. Сохранение состояния между сессиями работы с библиотекой не требуется.
К библиотеке должна поставляться простая программа, позволяющая добавить каталоги/файлы в индекс и сделать простые запросы.
