package com.mib.feature_home.presentation

import com.mib.lib_navigation.datatype.BottomNav
import java.util.EmptyStackException
import java.util.Stack

/**
 * Back stack contains the history of tabs that have been visited
 */

class HomeBackStack(initial: List<Int>) {

    private var stack: Stack<Int> = Stack()

    init {
        stack.addAll(ArrayList(initial))
    }

    @BottomNav.BottomNavType
    val currentPage: Int
        get() {
            return try {
                stack.peek()
            } catch (e: EmptyStackException) {
                BottomNav.TAB_UNDEFINED
            }
        }

    /**
     * An identical tab cannot be visited twice
     */
    fun visit(@BottomNav.BottomNavType id: Int) {
        if (stack.isEmpty() || currentPage != id) stack.push(id)
    }

    fun goBack(
        navigationAction: () -> Unit
    ) {
        try {
            stack.pop()
            navigationAction()
        } catch (e: EmptyStackException) {
            // do nothing
        }
    }

    fun isFirstPage() = stack.size == 1

    fun getStack(): List<Int> = stack.toList()
}