<template>
  <div class="grid grid-cols-3 sm:grid-cols-6 gap-3">
    <router-link
      v-for="category in categories"
      :key="category.id"
      :to="`/category/${category.id}`"
      class="rounded-2xl bg-white border border-slate-100 p-4 text-center hover:shadow-md hover:border-primary-200 transition"
    >
      <img
        v-if="category.icon"
        :src="category.icon"
        :alt="category.name"
        class="w-10 h-10 mx-auto rounded-xl object-cover"
      />
      <i v-else :class="[getCategoryIcon(category.id), 'text-2xl', getCategoryColor(category.id)]"></i>
      <p class="mt-2 text-sm text-slate-700">{{ category.name }}</p>
    </router-link>
  </div>
</template>

<script setup>
defineProps({
  categories: {
    type: Array,
    default: () => []
  }
})

function getCategoryIcon(id) {
  const icons = {
    1: 'fa-solid fa-sun',
    2: 'fa-solid fa-leaf',
    3: 'fa-solid fa-lemon',
    4: 'fa-solid fa-apple-whole',
    5: 'fa-solid fa-seedling',
    6: 'fa-solid fa-plane'
  }
  return icons[id] || 'fa-solid fa-apple-whole'
}

function getCategoryColor(id) {
  const colors = {
    1: 'text-amber-500',
    2: 'text-pink-500',
    3: 'text-yellow-500',
    4: 'text-rose-500',
    5: 'text-emerald-500',
    6: 'text-slate-500'
  }
  return colors[id] || 'text-primary-500'
}
</script>
