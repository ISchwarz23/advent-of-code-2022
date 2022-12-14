package utils

import java.io.File
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val DEFAULT_COOKIE_FILE = File("session.txt")

val aocClient = AocClient()

class AocClient(cookieFile: File = DEFAULT_COOKIE_FILE) {

    private val cookies = if (cookieFile.exists()) cookieFile.readLines()[0] else null
    private val client = HttpClient.newBuilder().build()

    fun submit(year: Int, day: Int, level: Int, answer: Long): String = submit(year, day, level, "$answer")

    fun submit(year: Int, day: Int, level: Int, answer: Int): String = submit(year, day, level, answer.toLong())

    fun submit(year: Int, day: Int, level: Int, answer: String): String {
        if (cookies == null) return "Unable to submit, as no session cookie provided"

        val values = mapOf("level" to "$level", "answer" to answer)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://adventofcode.com/$year/day/$day/answer"))
            .header("cookie", cookies)
            .POST(toFormData(values))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()


        val response = try {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (t: Throwable) {
            return "Unable to connect to AoC"
        }

        return if (response.statusCode() in 200..299) {
            val body = response.body().toString()
            if (body.contains("That's not the right answer")) {
                var wait = ""
                if (body.contains("please wait ")) {
                    wait += "; please wait "
                    wait += body.substringAfter("please wait ").substringBefore(" before trying again")
                }
                "Wrong answer$wait"
            } else if (body.contains("You gave an answer too recently")) {
                "Please wait ${body.substringAfter("You have ").substringBefore(" left to wait")}"
            } else if (body.contains("That's the right answer")) {
                "Correct answer"
            } else if (body.contains("You don't seem to be solving the right level")) {
                "Already submitted"
            } else {
                body
            }
        } else {
            "Unable to submit answer; Status: ${response.statusCode()}"
        }
    }

    private fun toFormData(data: Map<String, String>): HttpRequest.BodyPublisher? {
        val res = data.map { (k, v) -> "${(k.utf8())}=${v.utf8()}" }.joinToString("&")
        return HttpRequest.BodyPublishers.ofString(res)
    }

    private fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

}