object EventTestData {
    val validEventsAsJson: List<String> = listOf(
        """{"timestamp" : 1515609008,"userId" : 1123,"event" : "2 hours of downtime occurred due to the release of version 1.0.5 of the system"}""",
        """{"timestamp" : 1515649008,"userId" : 1153,"event" : "3 hours of downtime occurred due to the release of version 1.0.5 of the system"}"""
    )

    val singleEventAsJson: List<String> = listOf(
        """{"timestamp" : 1515609008,"userId" : 1123,"event" : "2 hours of downtime occurred due to the release of version 1.0.5 of the system"}"""
    )

    val emptyEventsAsJson: List<String> =
        listOf("""""")

    val invalidEventsAsJson: String =
        """[{
      |"timestaamp" : 1515609008,
      |"userId" : 1123,
      |"event" : "2 hours of downtime occurred due to the release of version 1.0.5 of the system"
      |}]""".trimMargin()

    val brokenEventsAsJson: String =
        """[|"userId" : 1123,
      |"event" : "2 hours of downtime occurred due to the release of version 1.0.5 of the system"
      |}]""".trimMargin()

}