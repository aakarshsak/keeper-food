import React, { useState } from 'react';

const SearchBar = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleInputChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    onSearch(value); // Real-time search
  };

  const handleClear = () => {
    setSearchTerm('');
    onSearch('');
  };

  return (
    <div className="search-container">
      <div className="card">
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <div style={{ position: 'relative', flex: 1 }}>
            <input
              type="text"
              value={searchTerm}
              onChange={handleInputChange}
              className="form-input"
              placeholder="Search food items by name or description..."
              style={{ paddingRight: searchTerm ? '40px' : '10px' }}
            />
            {searchTerm && (
              <button
                onClick={handleClear}
                style={{
                  position: 'absolute',
                  right: '10px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  background: 'none',
                  border: 'none',
                  cursor: 'pointer',
                  fontSize: '18px',
                  color: '#666',
                  padding: '0',
                  width: '20px',
                  height: '20px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
                title="Clear search"
              >
                ×
              </button>
            )}
          </div>
        </div>
        {searchTerm && (
          <p style={{ margin: '10px 0 0 0', color: '#666', fontSize: '14px' }}>
            Searching for: "{searchTerm}"
          </p>
        )}
      </div>
    </div>
  );
};

export default SearchBar;