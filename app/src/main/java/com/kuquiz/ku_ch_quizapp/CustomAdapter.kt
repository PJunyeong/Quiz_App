package com.kuquiz.ku_ch_quizapp

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_question.view.*


class CustomAdapter: RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<question>()
    var context:Any? = null
    var listAnswer = mutableMapOf("get_keys" to 0)
    var is_review = false
    var question_set = true
    var num = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val question = listData.get(position)

        if (question_set) {holder.setQuestion(question)} else {holder.setQuestionDetail(question, listAnswer)}

        if (is_review) {
            holder.itemView.question_review_cnt.text = "오답 ${question.cnt}회"
            holder.itemView.question_review_cnt.visibility = View.VISIBLE
            holder.itemView.question_del.visibility = View.VISIBLE
        }

        holder.itemView.question_choice_check.setOnCheckedChangeListener{group, checkedid -> when (checkedid){
            R.id.question_choice1 -> listAnswer[get_key(question)] = 1
            R.id.question_choice2 -> listAnswer[get_key(question)] = 2
            R.id.question_choice3 -> listAnswer[get_key(question)] = 3
            R.id.question_choice4 -> listAnswer[get_key(question)] = 4
        }
        }

        holder.itemView.question_toggle.setOnClickListener{
            if (holder.itemView.question_box.visibility == View.GONE){
                Log.d("toggle plus", "지문 박스 확인")
                holder.itemView.question_box.visibility = View.VISIBLE
                holder.itemView.question_toggle.setBackgroundResource(R.drawable.circle_minus)
            } else {
                Log.d("toggle minus", "지문 박스 줄이기")
                holder.itemView.question_box.visibility = View.GONE
                holder.itemView.question_toggle.setBackgroundResource(R.drawable.circle_plus)
            }
            notifyDataSetChanged()
        }

        holder.itemView.question_del.setOnClickListener {
            val test_num = question.test_num
            val number = question.number
            holder.itemView.question_choice1.setBackgroundColor(Color.WHITE)
            holder.itemView.question_choice2.setBackgroundColor(Color.WHITE)
            holder.itemView.question_choice3.setBackgroundColor(Color.WHITE)
            holder.itemView.question_choice4.setBackgroundColor(Color.WHITE)
            Log.d("item view del", "리사이클러뷰 아이템 삭제")
            val db_helper = DBHelper(context as Context)
            db_helper.delete_review(question.type!!, question.test_num!!, question.number!!)
            listData.remove(question)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listData.size)
        }


        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 1500


        holder.itemView.requestLayout()
    }

    override fun getItemCount(): Int {
        return listData.size
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

}

fun get_key(question: question): String {
    return question.test_num.toString() + ":" + question.number.toString()
}



class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {



    fun setQuestion(question: question) {
        itemView.question_test_num.text = "${question.test_num}회"
        itemView.question_number.text = "${question.number}번"
        itemView.question_choice1.text = "1번. ${question.choice1}"
        itemView.question_choice2.text = "2번. ${question.choice2}"
        itemView.question_choice3.text = "3번. ${question.choice3}"
        itemView.question_choice4.text = "4번. ${question.choice4}"

        // 유형 6인 경우에만 체크 박스 넣는다. 이 상태에서 아직 체크를 하지 않았으므로 box는 gone, 토글 스위치는 visible하게 바꿔준다.

        if (question.type == 6) {
            itemView.question_toggle.visibility = View.VISIBLE
            val resID = itemView.context.resources.getIdentifier("box_${question.test_num}_${question.order}", "drawable", itemView.context.packageName)
            itemView.question_box.setImageResource(resID)
        }


        if (question.choice3 == null) {
            Log.d("null!!", "question_choice3 checked")
            itemView.question_choice3.visibility = View.GONE
            itemView.question_choice4.visibility = View.GONE
        }


        when (question.type) {
            1 -> {
                itemView.question_question.text = "${question.question}의 독음이 바른 것은?"
            }
            2 -> {
                itemView.question_question.text = "${question.question}의 독음을 가진 것은?"
            }
            3 -> {
                itemView.question_question.text = "${question.question}의 독음이 같은 것은?"
            }
            4 -> {
                itemView.question_question.text = "${question.question}의 뜻이 바른 것은?"
            }
            5 -> {
                itemView.question_question.text = "${question.question}의 뜻을 가진 것은?"
            }
            6 -> {
                itemView.question_question.text = "${question.question}"
            }
        }
    }
    fun setQuestionDetail(question: question, listAnswer:MutableMap<String, Int>) {
        itemView.question_test_num.text = "${question.test_num}회"
        itemView.question_number.text = "${question.number}번"
        if (question.question_detail != null){
        itemView.question_detail.text = "${question.question_detail}"
        }

        if (question.type == 6) {
            itemView.question_toggle.visibility = View.VISIBLE
            val resID = itemView.context.resources.getIdentifier("box_${question.test_num}_${question.order}", "drawable", itemView.context.packageName)
            itemView.question_box.setImageResource(resID)
        }

        when (question.type) {
            1 -> {
                itemView.question_question.text = "${question.question}의 독음이 바른 것은?"
            }
            2 -> {
                itemView.question_question.text = "${question.question}의 독음을 가진 것은?"
            }
            3 -> {
                itemView.question_question.text = "${question.question}의 독음이 같은 것은?"
            }
            4 -> {
                itemView.question_question.text = "${question.question}의 뜻이 바른 것은?"
            }
            5 -> {
                itemView.question_question.text = "${question.question}의 뜻을 가진 것은?"
            }
            6 -> {
                itemView.question_question.text = "${question.question}"
            }
        }

        if (question.choice1_detail == null) {
            itemView.question_choice1.text = "1번. ${question.choice1}"
        } else {
            itemView.question_choice1.text = "1번. ${question.choice1} : ${question.choice1_detail}"
        }

        if (question.choice2_detail == null) {
            itemView.question_choice2.text = "2번. ${question.choice2}"
        } else {
            itemView.question_choice2.text = "2번. ${question.choice2} : ${question.choice2_detail}"
        }

        if (question.choice3_detail == null) {
            itemView.question_choice3.text = "3번. ${question.choice3}"
        } else {
            itemView.question_choice3.text = "3번. ${question.choice3} : ${question.choice3_detail}"
        }

        if (question.choice4_detail == null) {
            itemView.question_choice4.text = "4번. ${question.choice4}"
        } else {
            itemView.question_choice4.text = "4번. ${question.choice4} : ${question.choice4_detail}"
        }

        when (question.answer) {
            1 -> itemView.question_choice1.setBackgroundColor(Color.YELLOW)
            2 -> itemView.question_choice2.setBackgroundColor(Color.YELLOW)
            3 -> itemView.question_choice3.setBackgroundColor(Color.YELLOW)
            4 -> itemView.question_choice4.setBackgroundColor(Color.YELLOW)
        }

        Log.d("listAnswer", "${listAnswer.get(get_key(question))}")


        if (listAnswer.get(get_key(question)) == question.answer) {
            itemView.question_question.setTextColor(Color.BLUE)
        } else {
            itemView.question_question.setTextColor(Color.RED)
        }


    }

}





