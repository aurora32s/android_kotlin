package com.haman.aop_part5_chapter05.presentation

/**
 * view -> presenter
 */
interface BaseView<PresenterT: BasePresenter> {
    val presenter: PresenterT
}