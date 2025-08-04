import React, { useState } from 'react';

const FoodItemForm = ({ onSubmit }) => {
  // Get current date and time in local timezone for default value
  const getCurrentDateTime = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  };

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    consumedDate: getCurrentDateTime(),
    calorie: '',
    quantity: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Validate required fields
    if (!formData.name.trim()) {
      alert('Please enter a food name');
      return;
    }

    // Prepare data for submission
    const submissionData = {
      name: formData.name.trim(),
      description: formData.description.trim() || null,
      consumedDate: formData.consumedDate || null,
      calorie: formData.calorie ? parseInt(formData.calorie) : null,
      quantity: formData.quantity.trim() || null
    };

    onSubmit(submissionData);
    
    // Reset form
    setFormData({
      name: '',
      description: '',
      consumedDate: getCurrentDateTime(),
      calorie: '',
      quantity: ''
    });
  };



  return (
    <form onSubmit={handleSubmit}>
      <div className="form-row">
        <div className="form-group">
          <label htmlFor="name" className="form-label">
            Food Name *
          </label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="form-input"
            placeholder="e.g., Chicken Breast, Apple, Pizza"
            maxLength="100"
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="consumedDate" className="form-label">
            Date & Time Consumed
          </label>
          <input
            type="datetime-local"
            id="consumedDate"
            name="consumedDate"
            value={formData.consumedDate}
            onChange={handleChange}
            className="form-input"
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="calorie" className="form-label">
            Calories (Optional)
          </label>
          <input
            type="number"
            id="calorie"
            name="calorie"
            value={formData.calorie}
            onChange={handleChange}
            className="form-input"
            placeholder="e.g., 250"
            min="0"
            max="10000"
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="quantity" className="form-label">
            Quantity (Optional)
          </label>
          <input
            type="text"
            id="quantity"
            name="quantity"
            value={formData.quantity}
            onChange={handleChange}
            className="form-input"
            placeholder="e.g., 1 piece, 200g, 1 cup"
            maxLength="50"
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="description" className="form-label">
          Description (Optional)
        </label>
        <textarea
          id="description"
          name="description"
          value={formData.description}
          onChange={handleChange}
          className="form-input"
          placeholder="Additional notes about this food item..."
          maxLength="500"
          rows="3"
        />
      </div>

      <button type="submit" className="btn btn-primary">
        Add Food Item
      </button>
    </form>
  );
};

export default FoodItemForm;