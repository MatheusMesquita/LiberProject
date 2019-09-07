package matheus.liberproject.model

data class MovieResponse(val Search: List<Movie>, val Response: String, val totalResults: String)

data class Movie(val Title: String, val Year: String, val imdbID: String, val Type: String,
                 val Poster: String)