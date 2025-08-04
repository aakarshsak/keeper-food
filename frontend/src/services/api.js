import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Food Items API
export const foodItemsAPI = {
  // Get all food items
  getAll: () => api.get('/food-items'),
  
  // Get food item by ID
  getById: (id) => api.get(`/food-items/${id}`),
  
  // Create new food item
  create: (foodItem) => api.post('/food-items', foodItem),
  
  // Update food item
  update: (id, foodItem) => api.put(`/food-items/${id}`, foodItem),
  
  // Delete food item
  delete: (id) => api.delete(`/food-items/${id}`),
  
  // Search food items by name
  search: (name) => api.get(`/food-items/search?name=${encodeURIComponent(name)}`),
  
  // Get recent food items (last 7 days)
  getRecent: () => api.get('/food-items/recent'),
  
  // Get recently consumed food items
  getRecentlyConsumed: () => api.get('/food-items/recently-consumed'),
  
  // Get total count
  getCount: () => api.get('/food-items/count'),
};

export default api;