package com.haman.aop_part5_chapter06.presentation

interface BaseView<PresenterT: BasePresenter> {
    val presenter: PresenterT
}