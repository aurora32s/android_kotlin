package com.haman.aop_part5_chapter05.presentation

/**
 * MVP: Model <- Presenter <-interface-> View
 */
interface BaseView<PresenterT: BasePresenter> {
    val presenter: PresenterT
}