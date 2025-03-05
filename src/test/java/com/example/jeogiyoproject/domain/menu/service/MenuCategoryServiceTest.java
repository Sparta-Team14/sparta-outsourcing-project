package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryDeletedBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MenuCategoryServiceTest {
    @Mock
    private MenuCategoryRepository menuCategoryRepository;
    @Mock
    private FoodStoreRepository foodStoreRepository;
    @InjectMocks
    private MenuCategoryService menuCategoryService;

    @Nested
    class CreateMenuCategoryTest {
        @Test
        void 메뉴카테고리_생성_성공() {
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;
            String name = "주 메뉴";

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            given(foodStoreRepository.findById(foodStoreId)).willReturn(Optional.of(foodStore));

            // when
            MenuCategoryResponseDto responseDto = menuCategoryService.createCategory(userId, foodStoreId, name);

            // then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getName()).isEqualTo(name);
        }

        @Test
        void 메뉴카테고리_생성_가게조회_실패() {
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;
            String name = "주 메뉴";

            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.empty());
            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
                menuCategoryService.createCategory(userId,foodStoreId,name);
            });
            // then
            assertEquals(ErrorCode.FOODSTORE_NOT_FOUND.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴카테고리_생성_OWNER_ID_USER_ID_불일치() {
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;
            String name = "주 메뉴";
            Long ownerId = 2L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodStore));

            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
               menuCategoryService.createCategory(userId,foodStoreId,name);
            });
            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴카테고리_생성_카테고리_이름_중복() {
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;
            String name = "주 메뉴";

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);
            ReflectionTestUtils.setField(menuCategory,"name",name);

            given(foodStoreRepository.findById(foodStoreId)).willReturn(Optional.of(foodStore));
            given(menuCategoryRepository.existsByFoodStoreIdAndName(foodStoreId,name)).willReturn(true);

            //when
            CustomException exception = assertThrows(CustomException.class, ()->{
               menuCategoryService.createCategory(userId,foodStoreId,name);
            });

            //then
            assertEquals(ErrorCode.CATEGORY_IS_EXIST.getMessage(),exception.getMessage());
        }
    }

    @Nested
    class UpdateMenuCategoryTest{
        @Test
        void 메뉴카테고리_업데이트_성공() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            String name = "주 메뉴";
            Long ownerId = 1L;
            Long foodstoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodstoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);


            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            MenuCategoryResponseDto responseDto = menuCategoryService.updateCategory(userId,categoryId,name);

            // then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getName()).isEqualTo(name);
        }
        @Test
        void 메뉴카테고리_업데이트_카테고리_조회_실패(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            String name = "주 메뉴";

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, ()->{
                menuCategoryService.updateCategory(userId,categoryId,name);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴카테고리_업데이트_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            String name = "주 메뉴";
            Long ownerId = 2L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);


            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));
            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
                menuCategoryService.updateCategory(userId,foodStoreId,name);
            });
            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(), exception.getMessage());
        }
        @Test
        void 메뉴카테고리_업데이트_카테고리_이름_중복(){
            // given
            Long userId =  1L;
            Long categoryId = 1L;
            String name = "주 메뉴";
            Long foodStoreId = 1L;

            User user = new User();
            ReflectionTestUtils.setField(user,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",user);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));
            given(menuCategoryRepository.existsByFoodStoreIdAndName(foodStoreId,name)).willReturn(true);

            // when
            CustomException exception = assertThrows(CustomException.class, ()-> {
               menuCategoryService.updateCategory(userId, categoryId, name);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_IS_EXIST.getMessage(),exception.getMessage());
        }
    }


    @Nested
    class DeleteMenuCategoryTest{
        @Test
        void 메뉴카테고리_삭제_성공() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;
            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            MenuCategoryResponseDto responseDto = menuCategoryService.deleteCategory(userId,categoryId);

            // then
            assertThat(responseDto.getDeletedAt()).isNotNull();
        }
        @Test
        void 메뉴카테고리_삭제_카테고리_조회_실패(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, ()->{
               menuCategoryService.deleteCategory(userId,categoryId);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴카테고리_삭제_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long ownerId = 2L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);


            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));
            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
                menuCategoryService.deleteCategory(userId,categoryId);
            });
            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(), exception.getMessage());
        }
        @Test
        void 메뉴카테고리_삭제_이미_삭제된_카테고리(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;
            User owner = new User();
            LocalDateTime deletedAt = LocalDateTime.now();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);
            ReflectionTestUtils.setField(menuCategory,"deletedAt",deletedAt);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            CustomException exception = assertThrows(CustomException.class,() -> {
                menuCategoryService.deleteCategory(userId,categoryId);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_ALREADY_DELETED.getMessage(),exception.getMessage());

        }
    }


    @Nested
    class RestoreMenuCategory{
        @Test
        void 메뉴카테고리_복구_성공() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;
            LocalDateTime deletedAt = LocalDateTime.now();
            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);
            ReflectionTestUtils.setField(menuCategory,"deletedAt",deletedAt);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            MenuCategoryResponseDto responseDto = menuCategoryService.restoreCategory(userId,categoryId);

            // then
            assertThat(responseDto.getDeletedAt()).isNull();
        }
        @Test
        void 메뉴카테고리_복구_카테고리_조회_실패(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, ()->{
                menuCategoryService.restoreCategory(userId,categoryId);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴카테고리_복구_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long ownerId = 2L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);


            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));
            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
                menuCategoryService.restoreCategory(userId,categoryId);
            });
            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(), exception.getMessage());
        }
        @Test
        void 메뉴카테고리_복구_삭제되지_않은_카테고리(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(menuCategoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            CustomException exception = assertThrows(CustomException.class,() -> {
                menuCategoryService.restoreCategory(userId,categoryId);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_NOT_DELETED.getMessage(),exception.getMessage());
        }
    }

    @Nested
    class FindCategoryList{
        @Test
        void 메뉴카테고리_리스트_조회() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;
            String name = "주 메뉴";

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"name",name);

            List<MenuCategory> categories = List.of(menuCategory);
            given(menuCategoryRepository.findAllByFoodStoreIdAndDeletedAtIsNull(anyLong())).willReturn(categories);

            // when
            List<MenuCategoryBasicDto> list = menuCategoryService.findCategoryList(foodStoreId);

            // then
            assertEquals(1, list.size());
            assertEquals(menuCategory.getId(), list.get(0).getCategoryId());
            assertEquals(menuCategory.getName(),list.get(0).getName());
        }
    }

    @Nested
    class FindDeletedCategoryList{
        @Test
        void 메뉴카테고리_삭제된_리스트_조회() {
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;
            LocalDateTime deletedAt = LocalDateTime.now();
            String name = "주 메뉴";

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"name",name);
            ReflectionTestUtils.setField(menuCategory,"deletedAt",deletedAt);

            List<MenuCategory> categories = List.of(menuCategory);

            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodStore));
            given(menuCategoryRepository.findAllByFoodStoreIdAndDeletedAtIsNotNull(anyLong())).willReturn(categories);

            // when
            List<MenuCategoryDeletedBasicDto> list = menuCategoryService.findDeletedCategoryList(userId,foodStoreId);

            // then
            assertEquals(1, list.size());
            assertEquals(menuCategory.getId(), list.get(0).getCategoryId());
            assertEquals(menuCategory.getName(),list.get(0).getName());
            assertThat(list.get(0).getDeletedAt()).isNotNull();
        }
    }



}