package com.example.postapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postapp.data.PostRepository
import com.example.postapp.model.Post
import com.example.postapp.ui.theme.PostAppTheme
import com.example.postapp.viewmodel.PostViewModel
import com.example.postapp.viewmodel.PostViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {

                    val postViewModel: PostViewModel = viewModel(

                        factory = PostViewModelFactory(PostRepository())
                    )

                    val posts by postViewModel.posts.collectAsState()

                    val selectedPost by postViewModel.selectedPost.collectAsState()

                    val isEditing by postViewModel.isEditing.collectAsState()



                    PostsScreen(
                        posts = posts,
                        selectedPost = selectedPost,
                        onPostClick = { postViewModel.selectPost(it) },
                        onEditBtnClick = { postViewModel.toggleEditing() },
                        onTitleChange = { postViewModel.updateTitle(it) },
                        onBodyChange = { postViewModel.updateBody(it) },
                        isEditing = isEditing,
                        onSaveBtnClick = { postViewModel.savePost() }
                    )
                }
            }
        }
    }
}


@Composable
fun PostsScreen(

    posts: List<Post>,
    selectedPost: Post?,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onEditBtnClick: () -> Unit,
    onSaveBtnClick: () -> Unit,
    onPostClick: (Post) -> Unit,
    isEditing: Boolean,
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFBC8F8F))) {

        PostDescription(
            post = selectedPost,
            isEditing = isEditing,
            onEditBtnClick = onEditBtnClick,
            onTitleChange = onTitleChange,
            onDescChange = onBodyChange,
            onSaveBtnClick = onSaveBtnClick
        )

        PostsList(
            posts = posts,
            selectedPost = selectedPost,
            onPostClick = onPostClick
        )

    }
}

@Composable
fun PostsList(
    posts: List<Post>,
    selectedPost: Post?,
    onPostClick: (Post) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.posts_list_label),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        posts.forEach { post ->
            PostItem(
                post = post,
                isSelected = post == selectedPost,
                onCardClick = { onPostClick(post) }
            )
        }

    }
}

@Composable
fun PostItem(
    post: Post,
    isSelected: Boolean,
    onCardClick: () -> Unit
) {

    if(isSelected){

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCardClick),
            colors = CardDefaults.cardColors(Color(0xFFB0C4DE)),
            shape = RoundedCornerShape(30.dp),
        )

        {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Пост ${post.id}",
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = post.title,
                )

            }
        }
    }

    else{


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCardClick),
            colors = CardDefaults.cardColors(Color(0xFFE0FFFF)),
            shape = RoundedCornerShape(30.dp),
        )


        {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Пост ${post.id}",
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = post.title,
                )

            }
        }
    }

}

@Composable
fun PostDescription(
    post: Post?,
    isEditing: Boolean,
    onEditBtnClick: () -> Unit,
    onSaveBtnClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
) {


    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End
    ) {


        if (post != null) {
            Text(
                text = stringResource(R.string.post_desc) + " ${post.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }

        else{
            Text(
                text = stringResource(R.string.post_desc),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }

        if (post != null) {

            if (isEditing) {

                TextField(
                    value = post.title,
                    onValueChange = onTitleChange,
                    label = {
                        Text(
                            text = stringResource(R.string.post_title_label),
                            fontSize = 20.sp,
                        ) },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFE0FFFF), focusedContainerColor = Color(0xFFB0C4DE)),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                TextField(
                    value = post.body,
                    onValueChange = onDescChange,
                    label = {
                        Text(
                            text = stringResource(R.string.post_desc_label),
                            fontSize = 20.sp,
                        ) },

                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFE0FFFF), focusedContainerColor = Color(0xFFB0C4DE)),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Button(
                    onClick = onSaveBtnClick,
                    colors = ButtonDefaults.buttonColors(Color(0xFF008B8B)),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.save_button_label))
                }
            } else {

                Text(
                    text = stringResource(R.string.post_title_label),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = post.title,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )


                Text(
                    text = stringResource(R.string.post_desc_label),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )


                Text(
                    text = post.body,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )


                Button(
                    onClick = onEditBtnClick,
                    colors = ButtonDefaults.buttonColors(Color(0xFF008B8B)),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.edit_button_label))
                }
            }

        } else {
            Text(
                text = stringResource(R.string.no_selection_label),
            )
        }
    }
}