package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuUpdateRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuCategoryRepository categoryRepository;
    @Mock
    private FoodStoreRepository foodStoreRepository;
    @InjectMocks
    private MenuService menuService;

    @Nested
    class CreateMenuTest {
        @Test
        void 메뉴_생성_성공() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long foodStoreId = 1L;
            String name = "메뉴1";
            MenuRequestDto menuRequestDto = new MenuRequestDto(1L,name,"메뉴1 정보",12000);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            MenuResponseDto responseDto = menuService.createMenu(userId,menuRequestDto);
            // then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getName()).isEqualTo(name);
        }
        @Test
        void 메뉴_생성_카테고리_조회_실패() {
            // given
            Long userId = 1L;
            String name = "메뉴1";
            MenuRequestDto menuRequestDto = new MenuRequestDto(1L,name,"메뉴1 정보",12000);

            given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
                menuService.createMenu(userId,menuRequestDto);
            });

            // then
            assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴_생성_OWNER_ID_USER_ID_불일치() {
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;
            Long ownerId = 2L;
            Long categoryId = 1L;
            String name = "메뉴1";
            MenuRequestDto menuRequestDto = new MenuRequestDto(1L,name,"메뉴1 정보",12000);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);


            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));

            // when
            CustomException exception = assertThrows(CustomException.class, () ->{
                menuService.createMenu(userId,menuRequestDto);
            });
            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_생성_메뉴_이름_중복() {
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;
            String name = "메뉴1";
            MenuRequestDto menuRequestDto = new MenuRequestDto(1L,name,"메뉴1 정보",12000);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);
            ReflectionTestUtils.setField(menuCategory,"name",name);

            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(menuCategory));
            given(menuRepository.existsByMenuCategory_FoodStore_IdAndName(foodStoreId,name)).willReturn(true);

            //when
            CustomException exception = assertThrows(CustomException.class, ()->{
                menuService.createMenu(userId,menuRequestDto);
            });

            //then
            assertEquals(ErrorCode.MENU_IS_EXIST.getMessage(),exception.getMessage());
        }
    }

    @Nested
    class UpdateMenuTest {
        @Test
        void 메뉴_업데이트_성공() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            String name = "메뉴1";
            String info = "메뉴1정보";
            Integer price = 12000;
            MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(name,info,price);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"name","수정 전 메뉴명");
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            // when
            MenuResponseDto responseDto = menuService.updateMenu(userId,menuId,requestDto);

            // then
            assertThat(responseDto.getName()).isEqualTo(name);
        }
        @Test
        void 메뉴_업데이트_메뉴_조회_실패(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            String name = "메뉴1";
            String info = "메뉴1정보";
            Integer price = 12000;
            MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(name,info,price);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"name","수정 전 메뉴명");
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());
            // when

            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.updateMenu(userId, menuId, requestDto);
            });

            // then
            assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴_업데이트_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long ownerId = 2L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            String name = "메뉴1";
            String info = "메뉴1정보";
            Integer price = 12000;
            MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(name,info,price);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"name","수정 전 메뉴명");
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.updateMenu(userId, menuId, requestDto);
            });

            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴_업데이트_메뉴_이름_중복(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            String name = "메뉴1";
            String info = "메뉴1정보";
            Integer price = 12000;
            MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(name,info,price);

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"name",name);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(menuRepository.existsByMenuCategory_FoodStore_IdAndName(foodStoreId,name)).willReturn(true);

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.updateMenu(userId, menuId, requestDto);
            });

            // then
            assertEquals(ErrorCode.MENU_IS_EXIST.getMessage(),exception.getMessage());
        }
    }

    @Nested
    class DeletedMenuTest{
        @Test
        void 메뉴_삭제_성공() {
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            MenuResponseDto responseDto = menuService.deleteMenu(userId, menuId);

            // then
            assertThat(responseDto.getDeletedAt()).isNotNull();
        }
        @Test
        void 메뉴_삭제_메뉴_조회_실패(){
            // given
            Long userId = 1L;
            Long menuId = 1L;

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.deleteMenu(userId, menuId);
            });
            assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
        }
        @Test
        void 메뉴_삭제_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long ownerId = 2L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                menuService.deleteMenu(userId, menuId);
            });

            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴_삭제_이미_삭제된_메뉴(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            LocalDateTime deletedAt = LocalDateTime.now();

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);
            ReflectionTestUtils.setField(menu,"deletedAt", deletedAt);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.deleteMenu(userId,menuId);
            });

            // then
            assertEquals(ErrorCode.MENU_ALREADY_DELETED.getMessage(),exception.getMessage());
        }
    }


    @Nested
    class RestoreMenuTest{
        @Test
        void 메뉴_복구_성공(){
            // when
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            LocalDateTime deletedAt = LocalDateTime.now();

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);
            ReflectionTestUtils.setField(menu, "deletedAt",deletedAt);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            MenuResponseDto responseDto = menuService.restoreMenu(userId, menuId);

            // then
            assertThat(responseDto.getDeletedAt()).isNull();
        }
        @Test
        void 메뉴_복구_메뉴_조회_실패(){
            // given
            Long userId = 1L;
            Long menuId = 1L;

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                menuService.restoreMenu(userId, menuId);
            });
            assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
        }
        @Test
        void 메뉴_복구_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long ownerId = 2L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                menuService.restoreMenu(userId, menuId);
            });

            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(),exception.getMessage());
        }
        @Test
        void 메뉴_복구_삭제되지_않은_메뉴(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                menuService.restoreMenu(userId,menuId);
            });

            // then
            assertEquals(ErrorCode.MENU_NOT_DELETED.getMessage(),exception.getMessage());
        }
    }

    @Nested
    class FindMenuTest{
        @Test
        void 메뉴_단건_조회_성공(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            MenuResponseDto responseDto = menuService.findMenu(menuId);

            // then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getMenuId()).isEqualTo(menuId);
        }
        @Test
        void 메뉴_단건_조회_메뉴_조회_실패(){
            // given
            Long menuId = 1L;

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                menuService.findMenu(menuId);
            });
            assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
        }
        @Test
        void 메뉴_단건_조회_삭제된_메뉴_조회(){
            // given
            Long userId = 1L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;
            LocalDateTime deletedAt = LocalDateTime.now();
            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);
            ReflectionTestUtils.setField(menu, "deletedAt", deletedAt);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.findMenu(menuId);
            });

            // then
            assertEquals(ErrorCode.MENU_DELETED.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class FindMenuListTest{
        @Test
        void 메뉴_리스트_조회_성공(){
            Long foodStoreId = 1L;
            Long categoryId = 1L;
            String categoryName = "주 메뉴";
            Long menuId = 1L;
            String menuName = "양념치킨";
            String info = "메뉴정보";
            Integer price = 12000;

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id", foodStoreId);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id", categoryId);
            ReflectionTestUtils.setField(menuCategory,"name", categoryName);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"name",menuName);
            ReflectionTestUtils.setField(menu,"info",info);
            ReflectionTestUtils.setField(menu,"price",price);

            List<Menu> menus = List.of(menu);
            MenuCategoryListResponseDto responseDto = new MenuCategoryListResponseDto(categoryId, categoryName);
            List<MenuCategoryListResponseDto> list = List.of(responseDto);

            given(menuRepository.findCategoriesAndMenusByFoodStoreId(foodStoreId)).willReturn(list);
            given(menuRepository.findMenusByMenuCategoryIdAndDeletedAtIsNull(categoryId)).willReturn(menus);

            // when
            List<MenuCategoryListResponseDto> result = menuService.findMenuList(foodStoreId);

            assertEquals(1, result.size());
            assertEquals(categoryName,result.get(0).getName());
            assertEquals(menuName, result.get(0).getMenus().get(0).getName());
        }
    }

    @Nested
    class FindDeletedMenuListTest{
        @Test
        void 삭제된_메뉴_리스트_조회_성공(){
            Long userId= 1L;
            Long foodStoreId = 1L;
            Long categoryId = 1L;
            String categoryName = "주 메뉴";
            Long menuId = 1L;
            String menuName = "양념치킨";
            String info = "메뉴정보";
            Integer price = 12000;
            LocalDateTime deletedAt = LocalDateTime.now();

            User user = new User();
            ReflectionTestUtils.setField(user, "id",userId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore,"id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user", user);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id", categoryId);
            ReflectionTestUtils.setField(menuCategory,"name", categoryName);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"name",menuName);
            ReflectionTestUtils.setField(menu,"info",info);
            ReflectionTestUtils.setField(menu,"price",price);
            ReflectionTestUtils.setField(menu,"deletedAt",deletedAt);

            List<Menu> menus = List.of(menu);

            MenuCategoryListResponseDto responseDto = new MenuCategoryListResponseDto(categoryId, categoryName);
            List<MenuCategoryListResponseDto> list = List.of(responseDto);

            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodStore));
            given(menuRepository.findCategoriesByFoodStoreId(anyLong())).willReturn(list);
            given(menuRepository.findMenusByMenuCategoryIdAndDeletedAtIsNotNull(anyLong())).willReturn(menus);

            // when
            List<MenuCategoryListResponseDto> result = menuService.findDeletedMenuList(userId, foodStoreId);

            assertEquals(1, result.size());
            assertEquals(categoryName,result.get(0).getName());
            assertEquals(menuName, result.get(0).getMenus().get(0).getName());
        }
        @Test
        void 삭제된_메뉴_리스트_조회_가게_조회_실패(){
            // given
            Long userId = 1L;
            Long foodStoreId = 1L;

            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
               menuService.findDeletedMenuList(userId, foodStoreId);
            });

            // then
            assertEquals(ErrorCode.FOODSTORE_NOT_FOUND.getMessage(), exception.getMessage());
        }
        @Test
        void 삭제된_메뉴_리스트_조회_OWNER_ID_USER_ID_불일치(){
            // given
            Long userId = 1L;
            Long ownerId = 2L;
            Long categoryId = 1L;
            Long menuId = 1L;
            Long foodStoreId = 1L;

            User owner = new User();
            ReflectionTestUtils.setField(owner,"id",ownerId);

            FoodStore foodStore = new FoodStore();
            ReflectionTestUtils.setField(foodStore, "id", foodStoreId);
            ReflectionTestUtils.setField(foodStore,"user",owner);

            MenuCategory menuCategory = new MenuCategory();
            ReflectionTestUtils.setField(menuCategory,"id",categoryId);
            ReflectionTestUtils.setField(menuCategory,"foodStore",foodStore);

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu,"id",menuId);
            ReflectionTestUtils.setField(menu,"menuCategory",menuCategory);

            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodStore));

            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                menuService.findDeletedMenuList(userId, foodStoreId);
            });

            // then
            assertEquals(ErrorCode.NOT_FOODSTORE_OWNER.getMessage(),exception.getMessage());
        }
    }
}