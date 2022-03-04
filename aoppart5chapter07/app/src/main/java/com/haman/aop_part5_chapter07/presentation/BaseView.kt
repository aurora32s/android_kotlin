package com.haman.aop_part5_chapter07.presentation

interface BaseView<PresenterT: BasePresenter> {
    val presenter: PresenterT
}