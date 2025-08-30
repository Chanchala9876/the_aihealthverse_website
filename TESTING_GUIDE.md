# 🧪 Testing Guide - Enhanced Doctor-Patient Consultation System

## 🚀 **Quick Start Testing**

### **1. Start the Application**
```bash
mvnw.cmd spring-boot:run
```
Wait for the application to start on `http://localhost:8090`

### **2. Available Test Accounts**

#### 👨‍⚕️ **Doctor Accounts:**
```
Email: jhalkumari2003@gmail.com
Password: doctor123
Specialization: Mental Health

Email: sarah.wilson@healthverse.com  
Password: doctor123
Specialization: Mental Health

Email: rajesh.kumar@healthverse.com
Password: doctor123
Specialization: Mental Health
```

#### 👤 **Patient Account:** (Create your own via signup or use existing)

## 🧪 **Testing Scenarios**

### **Scenario 1: Complete Patient-Doctor Flow**

#### **Step 1: Patient Requests Consultation**
1. Open **Browser 1** (Chrome)
2. Go to `http://localhost:8090`
3. Sign up/Login as a patient
4. Navigate to **Mental Health** section
5. You'll see doctors with:
   - ✅ Working hours displayed
   - ✅ Consultation fees
   - ✅ Specializations
6. Click **"💬 Contact"** on any doctor
7. Write initial message: *"Hi, I'm feeling anxious lately and need some guidance"*
8. Click **"Send Request"**
9. ✅ **Expected**: Redirected to chat page immediately

#### **Step 2: Doctor Receives & Responds**
1. Open **Browser 2** (Firefox/Edge)
2. Go to `http://localhost:8090/doctor/login`
3. Login with any doctor credentials
4. ✅ **Expected**: See **Pending Requests** card shows "1"
5. Check **"Pending Requests"** tab
6. ✅ **Expected**: See the patient's request with:
   - 🧠 Mental Health badge
   - Patient name and email
   - Initial message
   - Request timestamp
7. Click **"Accept Request"**
8. ✅ **Expected**: Success message + moved to Active tab

#### **Step 3: Real-Time Chat Testing**
1. **Doctor (Browser 2)**: Go to **"Active Consultations"** tab
2. Click **"Start Chat"** button
3. ✅ **Expected**: Chat interface opens
4. **Patient (Browser 1)**: Should already be in chat page
5. **Test real-time messaging:**
   - Doctor types: *"Hello! I understand you're feeling anxious. Can you tell me more?"*
   - Patient replies: *"Yes, it's been happening for the past few weeks..."*
   - ✅ **Expected**: Messages appear instantly in both browsers

### **Scenario 2: Email Notification Testing**
1. Follow Scenario 1 steps 1-8
2. ✅ **Expected**: Doctor receives email at their registered email address
3. Email should contain:
   - Patient details
   - Initial message
   - Link to doctor portal

### **Scenario 3: Multi-Patient Testing**
1. Repeat Scenario 1 with different patient accounts
2. ✅ **Expected**: Doctor dashboard shows multiple pending requests
3. Test accepting/rejecting different requests

## 🐛 **Common Issues & Solutions**

### **Issue 1: No Pending Requests Showing**
- **Cause**: Security config blocking requests
- **Solution**: Restart application, check logs

### **Issue 2: Chat Not Working**
- **Cause**: WebSocket connection issues
- **Solution**: 
  - Check browser console for errors
  - Ensure both users are in same chat room
  - Refresh both browser pages

### **Issue 3: Email Not Sending**
- **Cause**: SMTP configuration
- **Solution**: Check `application.properties` email settings

## ✅ **Expected Results Checklist**

### **Mental Health Page (`/mental-health`)**
- [ ] Doctors displayed with working hours
- [ ] Consultation fees shown
- [ ] Specializations visible
- [ ] Contact modal opens properly

### **Doctor Dashboard (`/doctor/dashboard`)**
- [ ] Pending requests show with patient details
- [ ] Accept/Reject buttons work
- [ ] Active consultations display after acceptance
- [ ] Real-time updates after actions

### **Real-Time Chat**
- [ ] Messages send instantly
- [ ] Both participants see messages
- [ ] Chat history loads properly
- [ ] WebSocket connection stable

### **Email Notifications**
- [ ] Doctor receives email notification
- [ ] Email contains patient details
- [ ] Professional formatting

## 🎯 **Success Criteria**

✅ **Complete Flow Working**: Patient → Request → Email → Doctor Accept → Real-time Chat

✅ **UI Enhancements**: Working hours, fees, specializations displayed

✅ **Real-time Communication**: Instant messaging between doctor-patient

✅ **Database Integration**: All requests stored and tracked properly

---

**🎉 If all scenarios pass, the enhanced doctor-patient consultation system is working perfectly!**
