package com.haman.aop_part3_chapter06.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.haman.aop_part3_chapter06.DBKey
import com.haman.aop_part3_chapter06.R
import com.haman.aop_part3_chapter06.chatlist.ChatListItem
import com.haman.aop_part3_chapter06.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var articleAdapter : ArticleAdapter
    private lateinit var articleDB : DatabaseReference

    private val auth : FirebaseAuth by lazy { Firebase.auth }
    private val userDB : DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USERS)
    }

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            // class 를 넘겨주면 data class 에 알맞은 데이터를 매핑하고 ArticleModel 객체를 반환한다.
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return // 매핑에 실패하면 null 반환

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view 초기화, event 등록
        binding = FragmentHomeBinding.bind(view)

        // view 만 없어지고, fragment 객체는 그대로 재사용되기 때문에, 배열 초기화 필요
        articleList.clear()
        articleDB = Firebase.database.reference.child(DBKey.DB_ARTICLES)
        articleDB.addChildEventListener(listener)

        val recyclerView : RecyclerView = binding.articleRecyclerView
        articleAdapter = ArticleAdapter(
            onItemClicked = { articleModel ->
                if (auth.currentUser != null) {
                    // 로그인 함
                    if (auth.currentUser?.uid != articleModel.seller) {
                        val chatRoom = ChatListItem(
                            buyerId = auth.currentUser?.uid.orEmpty(),
                            sellerId = articleModel.seller,
                            itemsTitle = articleModel.title,
                            key = System.currentTimeMillis() // 채팅방을 만든 시점
                        )
                        userDB.child(auth.currentUser?.uid.orEmpty())
                            .child(DBKey.CHILD_CHAT)
                            .push()
                            .setValue(chatRoom)

                        userDB.child(articleModel.seller)
                            .child(DBKey.CHILD_CHAT)
                            .push()
                            .setValue(chatRoom)

                        Snackbar.make(view, "채팅방이 생성되었습니다. 채팅방에서 확인해주세요.", Snackbar.LENGTH_LONG).show()

                    } else {
                        Snackbar.make(view, "내가 올린 아이템입니다.", Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
                }
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(context) // getContext
        recyclerView.adapter = articleAdapter

        binding.addFloatingButton.setOnClickListener {
            context?.let {
                if (auth.currentUser != null) {
                    val intent = Intent(context, AddArticleActivity::class.java)
                    startActivity(intent)
                }
                // TODO 로그인 기능 구현 후에 주석 제거해주시
                else {
                    Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)
    }
}