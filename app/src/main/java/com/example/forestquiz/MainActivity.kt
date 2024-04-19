package com.example.forestquiz

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.forestquiz.ui.theme.ForestQuizTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForestQuizTheme {
                MyApp()

            }
        }
    }
}
@Composable
fun MyApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "frontPage"){
        composable("frontPage"){
            frontPage(onNavigateToLoading = {navController.navigate("loadingPage/$it")})
        }
        composable("loadingPage/{value}", arguments = listOf(navArgument("value"){
            type = NavType.IntType
        })){ it ->
            val value = it.arguments?.getInt("value")?:0
            loadingPage(value = value, onNavigateToChoice = { navController.navigate("choicepage/$it") }, onNavigateToChoice2 = {navController.navigate("choicePage2/$it")}, onNavigateToChoice3 = {navController.navigate("choicePage3/$it")})

        }
        composable("choicepage/{post}", arguments = listOf(navArgument("post"){
            type = PostType()
        })){ it ->
            val post = it.arguments?.getParcelable<Post>("post")
            choicePage(post = post, onNavigateToQuiz = {navController.navigate("quizPage/$it")}, onNavigateToHome = {navController.navigate("frontPage")})

        }

        composable("quizPage/{new_post}", arguments = listOf(navArgument("new_post"){
            type = PostType()

        }
        )){
            val post = it.arguments?.getParcelable<Post>("new_post")
            quizPage(post, onNavigateToHome = {navController.navigate("frontPage")})
        }
        composable("choicepage2/{post}", arguments = listOf(navArgument("post"){
            type = PostType()
        })){ it ->
            val post = it.arguments?.getParcelable<Post>("post")
            choicePage2(post = post, onNavigateToLearn = {navController.navigate("learnPage/$it")}, onNavigateToHome = {navController.navigate("frontPage")})

        }

        composable("learnPage/{new_post}", arguments = listOf(navArgument("new_post"){
            type = PostType()

        }
        )){
            val post = it.arguments?.getParcelable<Post>("new_post")
            //val num = it.arguments?.getInt("week_no")
            learnPage(post, onNavigateToHome = {navController.navigate("frontPage")})
        }

        composable("choicepage3/{post}", arguments = listOf(navArgument("post"){
            type = PostType()
        })){ it ->
            val post = it.arguments?.getParcelable<Post>("post")
            choicePage3(post = post, onNavigateToQuiz = {navController.navigate("quizPage/$it")}, onNavigateToHome = {navController.navigate("frontPage")})

        }
    }

}


@Composable
fun frontPage(onNavigateToLoading: (Int) -> Unit){
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Forest Quiz")

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
                    .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "NPTEL Course Quiz App- \nForests and their Management", fontWeight = FontWeight.Bold, fontSize = 18.sp )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(modifier = Modifier.width(200.dp), onClick = { onNavigateToLoading(0) }) {
                        Text(text = "Take Weekly Quiz", color = MaterialTheme.colorScheme.background)
                    }
                    Button(modifier = Modifier.width(200.dp), onClick = { onNavigateToLoading(2) }) {
                        Text(text = "Take Randomized Quiz", color = MaterialTheme.colorScheme.background)
                    }
                    Button(modifier = Modifier.width(150.dp), onClick = { onNavigateToLoading(1) }) {
                        Text(text = "Learn", color = MaterialTheme.colorScheme.background)
                    }

                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HyperlinkText(fullText = "Made by Ghouse\n", linkText = listOf("Ghouse"), hyperlinks = listOf("https://github.com/GhouseHimself"))
                    HyperlinkText(fullText = "Made using Jetpack Compose\n", linkText = listOf("Jetpack Compose"), hyperlinks = listOf("https://developer.android.com/develop/ui/compose"))

                }



            }
            

        }
    }
}


@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Cyan,
    linkTextFontWeight: FontWeight = FontWeight.Normal,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
){
    val annotatedString = buildAnnotatedString { append(fullText)
        addStyle(
            style = SpanStyle(
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            start = 0,
            end = fullText.length
        )

        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex
            )
        }
    }
    val urlHandler = LocalUriHandler.current
    ClickableText(modifier = modifier,text = annotatedString, onClick = {
        annotatedString.getStringAnnotations("URL", it, it).firstOrNull()?.let {stringAnnotation ->
            urlHandler.openUri(stringAnnotation.item)

        }
    })

}


@Composable
fun loadingPage(value:Int, onNavigateToChoice: (String) -> Unit, onNavigateToChoice2: (String) -> Unit, onNavigateToChoice3: (String) -> Unit){
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Loading")
            val qns = mutableListOf<qna>()

            //collect data from firebase
            val db = Firebase.firestore
            db.collection("questions").get().addOnSuccessListener { result->
                for(document in result){
                    qns.add(qna(document.data.get("Question").toString(), document.data.get("ans").toString(), document.data.get("op1").toString(), document.data.get("op2").toString(), document.data.get("op3").toString()))

                }
            }.addOnCompleteListener{
                if(value == 0){
                    val qns2: List<qna> = qns.toList()
                    val post = Post(1, "My Post", qns2)
                    val json = Uri.encode(Gson().toJson(post))
                    onNavigateToChoice(json)
                }
                else if(value == 2){
                    val qns2: List<qna> = qns.toList()
                    val post = Post(1, "My Post", qns2)
                    val json = Uri.encode(Gson().toJson(post))
                    onNavigateToChoice3(json)

                }
                else{
                    val qns2: List<qna> = qns.toList()
                    val post = Post(1, "My Post", qns2)
                    val json = Uri.encode(Gson().toJson(post))
                    onNavigateToChoice2(json)
                }

            }



        }
    }

}


@Composable
fun choicePage(post: Post?, onNavigateToQuiz: (String) -> Unit, onNavigateToHome: () -> Unit) {
    if (post != null) {


        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Card(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp) , colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Forest Quiz")

                }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
                    .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    
                }
                Column {
                    Button(onClick = {
                        val qns = post.content.slice(0..9)
                        val post2 = Post(2, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-1", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(10..19)
                        val post2 = Post(3, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-2", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(20..29)
                        val post2 = Post(5, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-3", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(30..39)
                        val post2 = Post(7, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-4", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(40..49)
                        val post2 = Post(11, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-5", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(50..59)
                        val post2 = Post(13, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-6", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(60..69)
                        val post2 = Post(17, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-7", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(70..79)
                        val post2 = Post(19, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-8", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(80..89)
                        val post2 = Post(23, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-9", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(90..99)
                        val post2 = Post(29, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-10", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(100..109)
                        val post2 = Post(31, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-11", color = MaterialTheme.colorScheme.background)
                    }
                    Button(onClick = {
                        val qns = post.content.slice(110..119)
                        val post2 = Post(37, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToQuiz(json)
                    }) {
                        Text(text = "Week-12", color = MaterialTheme.colorScheme.background)
                    }
                    
                }
                
                Column(modifier = Modifier.padding(20.dp)) {
                    Button(
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),onClick = { onNavigateToHome() }) {
                        Text(text = "Home Page", color = MaterialTheme.colorScheme.background)
                    }
                }


            }


            }

        }

    }
}


@Composable
fun choicePage2(post: Post?, onNavigateToLearn: (String) -> Unit, onNavigateToHome: () -> Unit){
    if (post != null) {


        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Card(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp) , colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Forest Quiz")

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
                        .background(color = MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column {

                    }
                    Column {Button(onClick = {
                        val qns = post.content.slice(0..9)
                        val post2 = Post(1, "My Post", qns)
                        val json = Uri.encode(Gson().toJson(post2))
                        onNavigateToLearn(json)
                    }) {
                        Text(text = "Week-1", color = MaterialTheme.colorScheme.background)
                    }
                        Button(onClick = {
                            val qns = post.content.slice(10..19)
                            val post2 = Post(2, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-2", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(20..29)
                            val post2 = Post(3, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-3", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(30..39)
                            val post2 = Post(4, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-4", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(40..49)
                            val post2 = Post(5, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-5", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(50..59)
                            val post2 = Post(6, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-6", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(60..69)
                            val post2 = Post(7, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-7", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(70..79)
                            val post2 = Post(8, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-8", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(80..89)
                            val post2 = Post(9, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-9", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(90..99)
                            val post2 = Post(10, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-10", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(100..109)
                            val post2 = Post(11, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-11", color = MaterialTheme.colorScheme.background)
                        }
                        Button(onClick = {
                            val qns = post.content.slice(110..119)
                            val post2 = Post(12, "My Post", qns)
                            val json = Uri.encode(Gson().toJson(post2))
                            onNavigateToLearn(json)
                        }) {
                            Text(text = "Week-12", color = MaterialTheme.colorScheme.background)
                        }

                    }

                    Column(modifier = Modifier.padding(20.dp)) {
                        Button(
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),onClick = { onNavigateToHome() }) {
                            Text(text = "Home Page", color = MaterialTheme.colorScheme.background)
                        }
                    }

                }


            }

        }

    }

}


@Composable
fun choicePage3(post: Post?, onNavigateToQuiz: (String) -> Unit, onNavigateToHome: () -> Unit){
    if (post != null) {
        var text by remember { mutableStateOf("10") }
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Card(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp) , colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Forest Quiz")

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
                        .background(color = MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.width(300.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        OutlinedTextField(
                            modifier = Modifier.width(300.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background
                            ),
                            label = { Text("Number of Questions", color = Color.Gray) },
                            value = text,
                            onValueChange = {
                                text = it
                            })
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            modifier = Modifier.width(300.dp),
                            shape = RectangleShape,onClick = {
                                val num = text.toInt()
                                val qns = post.content.shuffled().take(num)
                                val post2 = Post((40..100).random(), "My Post", qns)
                                val json = Uri.encode(Gson().toJson(post2))
                                onNavigateToQuiz(json)
                            }) {
                            Text("Go Random", color = MaterialTheme.colorScheme.background)
                        }

                        Spacer(modifier = Modifier.height(60.dp))

                    }
                    Column {
                        Button(
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),onClick = { onNavigateToHome() }) {
                            Text(text = "Home Page", color = MaterialTheme.colorScheme.background)
                        }

                    }


                }

            }


        }
    }


}


@Composable
fun learnPage(post:Post?, onNavigateToHome: () -> Unit){
    val qns = post?.content
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp) , colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Forest Quiz")

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
                    .background(color = MaterialTheme.colorScheme.background),
            ) {if(qns!=null){

                LazyColumn(contentPadding = PaddingValues(10.dp)) {
                    item {
                        Text("Week ${post.id}:-")
                        Text(" ")
                    }
                    items(qns.size) { it ->
                        var x = listOf<String>(
                            qns[it].a,
                            qns[it].o1,
                            qns[it].o2,
                            qns[it].o3
                        ).shuffled()

                        Text(text = "(Q${it+1}) " + qns.get(it).q)
                        Text(text = "(a) " + x[0])
                        Text(text = "(b) " + x[1])
                        Text(text = "(c) " + x[2])
                        Text(text = "(d) " + x[3])
                        Text(text = "Answer: ")
                        Text(text = qns[it].a)
                        Text(" ")


                    }
                    item{
                        Button(onClick = { onNavigateToHome() }) {
                            Text("Home Page", color = MaterialTheme.colorScheme.background)
                        }
                    }
                }

            }

            }


        }

    }


}


@Parcelize
data class Post(var id: Int,var title: String,var content: List<qna>) : Parcelable


class PostType : NavType<Post>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Post? {
        return bundle.getParcelable(key)
    }
    override fun parseValue(value: String): Post {
        return Gson().fromJson(value, Post::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: Post) {
        bundle.putParcelable(key, value)
    }
}


@Parcelize
data class qna(var q: String, var a: String, var o1: String, var o2: String, var o3: String) : Parcelable {}


@Composable
fun quizPage(post: Post?, onNavigateToHome: () -> Unit) //qns: MutableList<qna>
{
    val qns = post?.content
    var wrongs: MutableList<qna> = mutableListOf()
    var n by remember {
        mutableIntStateOf(0)
    }
    var correctAns by remember {
        mutableStateOf("")
    }
    var selectOpt by remember {
        mutableIntStateOf(0)
    }
    var submitted by remember {
        mutableStateOf(false)
    }
    var correctOpt by remember {
        mutableIntStateOf(0)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp) , colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Forest Quiz")

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
                    .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (qns != null) {
                    if (n < qns.size) {
                        var selectstate1 by remember {
                            mutableStateOf(false)
                        }
                        var selectstate2 by remember {
                            mutableStateOf(false)
                        }
                        var selectstate3 by remember {
                            mutableStateOf(false)
                        }
                        var selectstate4 by remember {
                            mutableStateOf(false)
                        }

                        var y = listOf<String>(qns[n].a, qns[n].o1, qns[n].o2, qns[n].o3)
                        val xt = post.id * (n + 1)
                        var x = y.shuffled(Random((xt)))
                        correctOpt = x.indexOf(qns[n].a) + 1
                        println(correctOpt)
                        Card {

                        }
                        Column(
                            modifier = Modifier
                                .width(300.dp)
                                .background(color = MaterialTheme.colorScheme.background)
                        ) {

                            Text(text = "(Q${n+1}) " + qns[n].q)
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(modifier = Modifier.fillMaxWidth(), shape = RectangleShape, border = BorderStroke(
                                width = 4.dp, color = if (selectstate1) {
                                    Color.Blue
                                } else {
                                    Color.Transparent
                                }
                            ), onClick = {
                                selectstate1 = true
                                selectstate2 = false
                                selectstate3 = false
                                selectstate4 = false
                                selectOpt = 1
                            }) {
                                Text(
                                    text = x[0], color = if (selectstate1) {
                                        Color.Blue
                                    } else {
                                         MaterialTheme.colorScheme.background
                                    }
                                )
                            }
                            Button(modifier = Modifier.fillMaxWidth(),shape = RectangleShape, border = BorderStroke(
                                width = 4.dp, color = if (selectstate2) {
                                    Color.Blue
                                } else {
                                    Color.Transparent
                                }
                            ), onClick = {
                                selectstate1 = false
                                selectstate2 = true
                                selectstate3 = false
                                selectstate4 = false
                                selectOpt = 2
                            }) {
                                Text(
                                    text = x[1], color = if (selectstate2) {
                                        Color.Blue
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    }
                                )
                            }
                            Button(modifier = Modifier.fillMaxWidth(),shape = RectangleShape, border = BorderStroke(
                                width = 4.dp, color = if (selectstate3) {
                                    Color.Blue
                                } else {
                                    Color.Transparent
                                }
                            ), onClick = {
                                selectstate1 = false
                                selectstate2 = false
                                selectstate3 = true
                                selectstate4 = false
                                selectOpt = 3
                            }) {
                                Text(
                                    text = x[2], color = if (selectstate3) {
                                        Color.Blue
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    }
                                )
                            }
                            Button(modifier = Modifier.fillMaxWidth(),shape = RectangleShape, border = BorderStroke(
                                width = 4.dp, color = if (selectstate4) {
                                    Color.Blue
                                } else {
                                    Color.Transparent
                                }
                            ), onClick = {
                                selectstate1 = false
                                selectstate2 = false
                                selectstate3 = false
                                selectstate4 = true
                                selectOpt = 4
                            }) {
                                Text(
                                    text = x[3], color = if (selectstate4) {
                                        Color.Blue
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    }
                                )
                            }
                            Text(correctAns)

                        }
                        Button(onClick = {
                            if (submitted) {

                                selectstate1 = false
                                selectstate2 = false
                                selectstate3 = false
                                selectstate4 = false
                                correctAns = ""
                                selectOpt = 0
                                correctOpt = 0
                                submitted = false
                                n += 1
                                //move to next question
                            } else {
                                if (selectOpt == correctOpt) {
                                    correctAns =
                                        "Your Answer is correct!!\n" + "Answer: " + qns[n].a
                                } else {
                                    correctAns = "Your Answer is wrong :(\n" + "Answer: " + qns[n].a
                                    wrongs.add(qns[n])
                                }
                                submitted = true

                            }
                        }) {
                            Text(
                                text = if (submitted) {
                                    "Next"
                                } else {
                                    "Submit"
                                }, color = MaterialTheme.colorScheme.background
                            )
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Text("Total Questions: ${qns.size}")
                    } else {
                        println("wrong size:" + wrongs.size)
                        LazyColumn(contentPadding = PaddingValues(10.dp)) {
                            item {
                                Text("Marks Scored: ${qns.size-wrongs.size}/${qns.size}")
                                Text("Your Mistakes:")
                                Text(" ")
                            }
                            items(wrongs.size) { it ->
                                var x = listOf<String>(
                                    wrongs[it].a,
                                    wrongs[it].o1,
                                    wrongs[it].o2,
                                    wrongs[it].o3
                                ).shuffled()
                                Text(text = "(Q) " + wrongs.get(it).q)
                                Text(text = "(a) " + x[0])
                                Text(text = "(b) " + x[1])
                                Text(text = "(c) " + x[2])
                                Text(text = "(d) " + x[3])
                                Text(text = "Answer: ")
                                Text(text = wrongs[it].a)
                                Text(" ")


                            }
                            item{
                                Button(onClick = { onNavigateToHome() }) {
                                    Text("Home Page", color = MaterialTheme.colorScheme.background)
                                }
                            }
                        }
                    }

                }


            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ForestPreview() {
    ForestQuizTheme {
        //frontPage()
    }
}