package com.haman.aop_part6_chapter01.data

import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.data.repository.RestaurantRepository
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory

class TestRestaurantRepository: RestaurantRepository {
    override suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity> {
        return when(restaurantCategory) {
            RestaurantCategory.ALL -> {
                listOf( // mocking data
                    RestaurantEntity(
                        id = 0,
                        restaurantInfoId = 0,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마포화로집",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 1,
                        restaurantInfoId = 1,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "옛날우동&덮밥",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 2,
                        restaurantInfoId = 2,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마스터석쇠불고기&냉면plus",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 3,
                        restaurantInfoId = 3,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마스터통삼겹",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 4,
                        restaurantInfoId = 4,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "창영이 족발&보쌈",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 5,
                        restaurantInfoId = 5,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 6,
                        restaurantInfoId = 6,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "김여사 칼국수&냉면 논현점",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 7,
                        restaurantInfoId = 7,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "돈키호테",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    )
                )
            }
            RestaurantCategory.KOREAN_FOOD -> {
                listOf( // mocking data
                    RestaurantEntity(
                        id = 0,
                        restaurantInfoId = 0,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마포화로집",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 1,
                        restaurantInfoId = 1,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "옛날우동&덮밥",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 2,
                        restaurantInfoId = 2,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마스터석쇠불고기&냉면plus",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 3,
                        restaurantInfoId = 3,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마스터통삼겹",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 4,
                        restaurantInfoId = 4,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "창영이 족발&보쌈",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 5,
                        restaurantInfoId = 5,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 6,
                        restaurantInfoId = 6,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "김여사 칼국수&냉면 논현점",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 7,
                        restaurantInfoId = 7,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "돈키호테",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    )
                )
            }
            RestaurantCategory.DUMPLING_FOOD -> {
                listOf( // mocking data
                    RestaurantEntity(
                        id = 0,
                        restaurantInfoId = 0,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마포화로집",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 1,
                        restaurantInfoId = 1,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "옛날우동&덮밥",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 2,
                        restaurantInfoId = 2,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마스터석쇠불고기&냉면plus",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 3,
                        restaurantInfoId = 3,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "마스터통삼겹",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 4,
                        restaurantInfoId = 4,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "창영이 족발&보쌈",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 5,
                        restaurantInfoId = 5,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 6,
                        restaurantInfoId = 6,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "김여사 칼국수&냉면 논현점",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    ),
                    RestaurantEntity(
                        id = 7,
                        restaurantInfoId = 7,
                        restaurantCategory = RestaurantCategory.ALL,
                        restaurantTitle = "돈키호테",
                        restaurantImageUrl = "https://picsum.photos/200",
                        grade = (1 until 5).random() + ((0..10).random() / 10f),
                        reviewCount = (0 until 200).random(),
                        deliveryTimeRange = Pair(0, 20),
                        deliveryTipRange = Pair(0, 2000),
                        restaurantTelNumber = "012-345-6789"
                    )
                )
            }
            else -> listOf()
        }
    }
}