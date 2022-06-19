package cn.bluesadi.fakedefender.util.media

import com.xuexiang.xui.utils.ResUtils
import java.io.BufferedReader
import java.io.InputStreamReader

/*
* 敏感词识别
* */
object SensitiveWordDetector {

    data class DFANode(val nextMap: MutableMap<Char, DFANode>, var isEnd: Boolean)

    private val DFA = mutableMapOf<Char, DFANode>()

    fun init(){
        val input = ResUtils.getResources().assets.open("words.txt")
        BufferedReader(InputStreamReader(input)).lines().forEach { addWord(it) }
    }

    fun addWord(word: String){
        var node = DFANode(DFA, false)
        for(chr in word){
            if(chr !in node.nextMap){
                val tmp = DFANode(mutableMapOf(), false)
                node.nextMap[chr] = tmp
                node = tmp
            }else{
                node = node.nextMap[chr]!!
            }
        }
        node.isEnd = true
    }

    fun detect(sentence: String) : String?{
        var node = DFANode(DFA, false)
        var word = ""
        for(chr in sentence){
            if(chr !in node.nextMap) {
                node = DFANode(DFA, false)
                word = ""
            }else{
                node = node.nextMap[chr]!!
                word += chr
                if(node.isEnd) return word
            }
        }
        return null
    }

}