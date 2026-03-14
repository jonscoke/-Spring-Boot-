<template>
  <div class="food-search">
    <el-input v-model="keyword" placeholder="按名称检索食物" clearable @keyup.enter="handleSearch">
      <template #append>
        <el-button @click="handleSearch">搜索</el-button>
      </template>
    </el-input>

    <div class="food-search__list">
      <el-empty v-if="!foods.length" description="暂无食物结果" />
      <div v-else class="food-search__grid">
        <button v-for="food in foods" :key="food.id" class="food-chip" type="button" @click="emit('select', food)">
          <strong>{{ food.foodName }}</strong>
          <span>{{ food.caloriePer100g }} kcal / 100g</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { searchFoodApi } from '@/api'
import type { FoodItem } from '@/types/api'

const emit = defineEmits<{
  select: [food: FoodItem]
}>()

const keyword = ref('')
const foods = ref<FoodItem[]>([])

async function handleSearch() {
  if (!keyword.value.trim()) {
    foods.value = []
    return
  }
  foods.value = await searchFoodApi(keyword.value.trim())
}
</script>
