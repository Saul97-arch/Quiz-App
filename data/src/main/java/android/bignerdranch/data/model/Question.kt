package android.bignerdranch.data.model

data class Question(
    val question: String,
    val isAnswerTrue: Boolean,
    var isCheaterOnQuestion: Boolean
) {
    // Em um cenário em que temos um banco de dados real
    // mantemos apenas uma isntacia de um objecto Question
    // Uma vez que construirmos o mesmo aí sim enviamos ao bacno de dados
    companion object {
        val questionBank = arrayOf(
            Question(
                "Canberra is the capital of Australia.",
                isAnswerTrue = true,
                isCheaterOnQuestion = false
            ),
            Question(
                "The Pacific Ocean is larger than the Atlantic Ocean.",
                isAnswerTrue = true,
                isCheaterOnQuestion = false
            ),
            Question(
                "he Suez Canal connects the Red Sea and the Indian Ocean.",
                isAnswerTrue = false,
                isCheaterOnQuestion = false
            ),
            Question(
                "The Amazon River is the longest river in the Americas.",
                isAnswerTrue = true,
                isCheaterOnQuestion = false
            ),
            Question(
                "Lake Baikal is the world\\'s oldest and deepest freshwater lake.",
                isAnswerTrue = true,
                isCheaterOnQuestion = false
            )
        )
    }
}



