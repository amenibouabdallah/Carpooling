import React, { useState } from 'react';
import './signup.css';
import logo from '../../../assets/logo.png';

const SignUp = () => {
  const [formData, setFormData] = useState({
    fullname: '',
    email: '',
    password: '',
    phone: '',
    address: '',
    role: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleLogoClick = () => {
    window.location.href = "/";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: formData.fullname,
          phoneNumber: formData.phone,
          email: formData.email,
          password: formData.password,
          address: formData.address,
          role: formData.role,
        }),
      });

      const result = await response.json();
      if (response.ok) {
        alert(result.message);
      } else {
        alert(result.message);
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Failed to register user.');
    }
  };

  return (
    <div className='login-containerr'>
<div className='loginImage'>
          <div className='loginText'>
            <img src={logo} alt='logo' id='logo' onClick={handleLogoClick}/>
            <h3>
              Best Carpool Website
            </h3>
          </div>
        </div>
        <div className="signup-container">
        <h3>
              Hello!
            </h3>
            <p>Sign up to get started</p>
      <form onSubmit={handleSubmit} className="signup-form">
        <div className="form-group">
          <input
            type="text"
            placeholder="Full Name"
            id="fullname"
            name="fullname"
            value={formData.fullname}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <input
            type="email"
            placeholder='Email'
            id="email-signup"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <input
            type="password"
            placeholder='Password'
            id="password-signup"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <input
            type="tel"
            placeholder='Phone Number'
            id="phone"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <input
          placeholder='Address'
            type="text"
            id="address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <select
          placeholder='Role'
            id="role"
            name="role"
            value={formData.role}
            onChange={handleChange}
            required
          >
            <option value="" disabled>Choose a role</option>
            <option value="passager">Passenger</option>
            <option value="driver">Driver</option>
          </select>
        </div>

        <button type="submit" className="signup-button">
          Sign Up
        </button>
      </form>
      <p>Already have an account ? 
      <a href="/login" className="login-link">Login</a>
      </p>
    </div>
        </div>
  );
};

export default SignUp;
