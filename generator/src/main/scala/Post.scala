import Drone._

/* Added for sending to handler */
import org.apache.commons._
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.entity.StringEntity

class Post() {

    def sendToBase(json_string: String) = {
        val url = "http://localhost:9000/msg";
        val post = new HttpPost(url)
        val client = new DefaultHttpClient
        println(json_string)
        post.setHeader("content-type", "application/json")
        post.setEntity(new StringEntity(json_string))
        val response = client.execute(post)
        //println("--- HEADERS ---")
        //response.getAllHeaders.foreach(arg => println(arg))
    }
}
