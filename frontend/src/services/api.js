import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle authentication errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Authentication API
export const authAPI = {
  register: (userData) => api.post('/auth/register', userData),
  login: (credentials) => api.post('/auth/login', credentials),
  logout: () => api.post('/auth/logout'),
  verifyEmail: (verificationData) => api.post('/auth/verify-email', verificationData),
  resendVerification: (emailData) => api.post('/auth/resend-verification', emailData),
  forgotPassword: (emailData) => api.post('/auth/forgot-password', emailData),
  resetPassword: (resetData) => api.post('/auth/reset-password', resetData),
  getCurrentUser: () => api.get('/auth/me'),
};

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
  
  // Export food items to CSV
  exportToCSV: (startDate = null, endDate = null) => {
    const params = new URLSearchParams();
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);
    
    return api.get(`/food-items/export?${params.toString()}`, {
      responseType: 'blob',
    });
  },
};

export default api;