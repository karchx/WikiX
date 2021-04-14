# ViewBinding
Документация по ViewBinding лежит https://developer.android.com/topic/libraries/view-binding
Там все довольно просто, коротко и достаточно для понимания (подробностей особых нет).
ViewBinding используется, чтобы ссылаться на View (Button, TextView...) без findViewById,
это экономит CPU во время работы приложения (не нужно делать поиск) и еще ViewBinding
удобнее.
Для работы с ViewBinding его нужно
1. разрешить для проекта
2. возможно, пересобрать проект, чтобы AndroidStudio сгенерировала вспомогательные классы
3. сделать переменную binding как поле класса Fragment
4. инициализировать binding в onCreateView и удалять в onDestroyView
5. получать доступ ко всем View, у которых есть id, описанным в layout файле (XML)
   через переменную binding.
Для включения ViewBinding нужно добавить в build.gradle приложения (не проекта)
android {
  ...
  buildFeatures {
      viewBinding true
  }
}
После этого, для каждого XML файла (layout) будут сгенерированы вспомогательные классы,
которые упростят inflate UI фрагмента и будут хранить ссылки на каждый View, для которого
есть id (в XML файле).