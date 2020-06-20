package matheus.liberproject.model

data class MovieResponse(val Search: List<Movie>, val Response: String, val totalResults: String)

data class Movie(val Title: String, val Year: String, val imdbID: String, val Type: String,
                 val Poster: String)

data class Ratings(val Source: String, val Value: String)

data class MovieDetails(
    val Title: String,
    val Year: String,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Writer: String,
    val Actors: String,
    val Plot: String,
    val Language: String,
    val Country: String,
    val Awards: String,
    val Poster: String,
    val Ratings: List<Ratings>,
    val Metascore: String,
    val imdbRating: String,
    val imdbVotes: String,
    val imdbID: String,
    val Type: String,
    val DVD: String,
    val BoxOffice: String,
    val Production: String,
    val Website: String,
    val Response: String
)