package com.haman.aop_part6_chapter01.viewmodel.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.haman.aop_part6_chapter01.data.entity.impl.OrderEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.data.repository.food.RestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.repository.order.DefaultOrderRepository
import com.haman.aop_part6_chapter01.data.repository.order.OrderRepository
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.screen.order.OrderMenuListViewModel
import com.haman.aop_part6_chapter01.screen.order.OrderMenuState
import com.haman.aop_part6_chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

class OrderMenuListViewModelTest : ViewModelTest() {

    @Mock
    lateinit var firebaseAuth: FirebaseAuth

    @Mock
    lateinit var firebaseUser: FirebaseUser

    private val restaurantFoodRepository by inject<RestaurantFoodRepository>()
    private val orderRepository by inject<OrderRepository>()

    private val restaurantId = 0L
    private val restaurantTitle = "식당명"

    private val orderMenuListViewModel by inject<OrderMenuListViewModel>()

    /**
     * 1. 장바구니에 메뉴를 담았을 때 정상적으로 구동했는지 검사
     * 2. 장바구니에 담은 메뉴를 리스트로 뿌려준다.
     * 3. 장바구니 목록에 있는 데이터를 갖고 주문을 한다.
     */

    @Test
    fun `insert food menus in basket`() = runBlocking {
        (0 until 10).forEach {
            restaurantFoodRepository.insertFoodMenuInBasket(
                RestaurantFoodEntity(
                    id = it.toString(),
                    title = "메뉴 $it",
                    description = "소개 $it",
                    price = it,
                    imageUrl = "",
                    restaurantId = restaurantId,
                    restaurantTitle = restaurantTitle
                )
            )
        }

        assert(restaurantFoodRepository.getAllFoodMenuListInBasket().size == 10)
    }

    @Test
    fun `test load order menu list`() = runBlocking {
        `insert food menus in basket`()
        val testObservable = orderMenuListViewModel.orderStateLiveData.test()
        orderMenuListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.UnInitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelIst = restaurantFoodRepository.getAllFoodMenuListInBasket()
                        .map {
                            FoodModel(
                                id = it.hashCode().toLong(),
                                type = CellType.ORDER_FOOD_CELL,
                                title = it.title,
                                description = it.description,
                                price = it.price,
                                imageUrl = it.imageUrl,
                                restaurantId = it.restaurantId,
                                foodId = it.id,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                )
            )
        )
    }

    @Test
    fun `test do order menu list`() = runBlocking {
        `insert food menus in basket`()
        val userId = "asdf"
        // what is mockito
        Mockito.`when`(firebaseAuth.currentUser).then { firebaseUser }
        Mockito.`when`(firebaseUser.uid).then { userId }

        val testObservable = orderMenuListViewModel.orderStateLiveData.test()
        orderMenuListViewModel.fetchData()

        val menuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket().map { it.copy() }
        val menuListInBasketModelList = menuListInBasket.map {
            FoodModel(
                id = it.hashCode().toLong(),
                type = CellType.ORDER_FOOD_CELL,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }

        orderMenuListViewModel.orderMenu()
        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.UnInitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelIst = menuListInBasketModelList
                ),
                OrderMenuState.Order
            )
        )

        assert(orderRepository.getAllOrderMenus(userId) is DefaultOrderRepository.Result.Success<*>)
        val result =
            orderRepository.getAllOrderMenus(userId) as DefaultOrderRepository.Result.Success<*>.data
        assert(
            result?.equals(
                listOf(
                    OrderEntity(
                        id = 0.toString,
                        userId = userId,
                        restaurantId = restaurantId,
                        foodMenuList = menuListInBasket,
                        restaurantTitle = restaurantTitle
                    )
                )
            ) ?: false
        )
    }
}