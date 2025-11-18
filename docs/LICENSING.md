# Licensing & Pricing

## Overview

Intel DPC++ CLion Plugin is available in three editions to suit different needs:

| Feature | Community | Professional | Enterprise |
|---------|-----------|--------------|------------|
| **Price** | Free | $99/year | $499/year |
| **License Type** | Apache 2.0 | Commercial | Commercial |
| **Support** | Community | Email (24h) | Priority (4h) |

---

## 🆓 Community Edition (Free)

### License
- **Apache 2.0 License** - fully open source
- Free for personal and commercial use
- No attribution required (but appreciated!)

### Features
✅ SYCL 2020 syntax highlighting  
✅ Code completion for SYCL APIs  
✅ Live templates (syclmain, syclqueue, etc.)  
✅ File type support (.dpcpp, .dp.cpp)  
✅ CMake toolchain detection  
✅ Basic documentation provider  
✅ Tools menu integration  

### Limitations
❌ No GPU debugging  
❌ No VTune profiling integration  
❌ No Device Explorer  
❌ No advanced code analysis  
❌ Community support only (GitHub issues)  

### Installation
```bash
# CLion Plugin Marketplace
Settings → Plugins → Search "Intel DPC++" → Install
```

---

## 💼 Professional Edition

### Price
- **$99 per user per year**
- 30-day free trial available
- Annual billing only

### Volume Discounts
- 5-9 licenses: 10% off ($89/year each)
- 10-24 licenses: 20% off ($79/year each)
- 25+ licenses: 30% off ($69/year each)

### Features
All Community features **plus**:

#### 🐛 GPU Debugging
- gdb-oneapi integration
- GPU thread inspector
- Device memory viewer
- Kernel breakpoints
- Conditional GPU breakpoints

#### ⚡ Performance Profiling
- Intel VTune Profiler integration
- GPU hotspots analysis
- GPU offload analysis
- Kernel performance metrics
- Memory bandwidth analysis

#### 📊 Device Explorer
- Real-time device monitoring
- Platform detection (Level Zero, OpenCL, CUDA)
- Device capability inspection
- Memory usage statistics

#### 🔬 Advanced Code Analysis
- SYCL code quality inspections
- Performance optimization hints
- Race condition detection
- Memory pattern analysis
- USM best practice checks

#### 📞 Priority Support
- Email support (24-hour response)
- Bug fix priority
- Feature request priority

### Licensing
- **Per-user licensing** - one license per developer
- Named user (not floating)
- Works on unlimited machines for same user
- Annual renewal required

### Activation
1. Purchase license key from website
2. Help → Register → Intel DPC++ Professional
3. Enter license key
4. Restart CLion

### Trial
```
Help → Register → Intel DPC++ Professional
→ Start Free Trial (30 days, no credit card)
```

---

## 🏢 Enterprise Edition

### Price
- **$499 per year** (unlimited seats)
- Custom pricing for 100+ developer teams
- Annual or multi-year agreements

### Features
All Professional features **plus**:

#### 👥 Unlimited Seats
- Organization-wide deployment
- No per-user limits
- Site license

#### 🚀 Priority Support
- **4-hour response time** (business hours)
- Dedicated Slack channel
- Direct engineer access
- Quarterly roadmap review

#### 🎓 Training & Onboarding
- 2 hours of custom training (per year)
- Onboarding sessions for new team members
- Architecture review (1 session per year)
- Best practices consultation

#### 🛠️ Custom Features
- Feature development priority
- Custom integration support
- Private plugin builds
- Early access to new features

#### 📈 Analytics & Reporting
- Usage statistics dashboard
- Team productivity metrics
- License utilization reports

### Licensing
- **Site license** - unlimited users within organization
- Covers all developers in company
- Annual renewal with volume lock-in

### Deployment
- Managed plugin repository
- Automated license distribution
- Group policy integration

### Activation
1. Contact sales: jakub.jirak@pm.me
2. Receive organization license key
3. Deploy via settings repository:
   ```xml
   <!-- settings.xml -->
   <component name="DpcppPluginSettings">
     <option name="licenseKey" value="ENT-XXXX-YYYY-ZZZZ"/>
   </component>
   ```

---

## 🎓 Academic & Non-Profit Discount

### Eligibility
- Valid .edu email address
- Non-profit 501(c)(3) status
- Research institution affiliation

### Discount
- **50% off Professional Edition**
- $49/year per user

### Application
Email jakub.jirak@pm.me with:
- Institution name
- Proof of affiliation (.edu email or official document)
- Number of licenses needed

---

## 📊 Feature Comparison Matrix

| Feature | Community | Professional | Enterprise |
|---------|-----------|--------------|------------|
| **Core Features** | | | |
| Syntax highlighting | ✅ | ✅ | ✅ |
| Code completion | ✅ | ✅ | ✅ |
| Live templates | ✅ | ✅ | ✅ |
| CMake integration | ✅ | ✅ | ✅ |
| **Advanced Features** | | | |
| GPU debugging | ❌ | ✅ | ✅ |
| VTune profiling | ❌ | ✅ | ✅ |
| Device explorer | ❌ | ✅ | ✅ |
| Code analysis | ❌ | ✅ | ✅ |
| **Support** | | | |
| Community (GitHub) | ✅ | ✅ | ✅ |
| Email support | ❌ | 24h | 4h |
| Slack channel | ❌ | ❌ | ✅ |
| Training | ❌ | ❌ | 2h/year |
| **Licensing** | | | |
| Users | Unlimited | Per-user | Unlimited |
| Machines | Unlimited | Unlimited | Unlimited |
| Source code access | ✅ | ❌ | ❌ |

---

## 🔄 Upgrade Path

### Community → Professional
1. Purchase Professional license
2. Enter license key in IDE
3. Restart CLion
4. Premium features automatically enabled

### Professional → Enterprise
1. Contact sales for organization license
2. Receive site license key
3. Deploy to team via settings repository
4. Access training & priority support

### Downgrade Policy
- Professional → Community: Automatic at subscription end
- Premium features become view-only for 30 days
- No data loss, project files remain compatible

---

## 💳 Payment & Billing

### Accepted Methods
- Credit/debit card (Stripe)
- Purchase order (Enterprise only, $1000+ orders)
- Wire transfer (Enterprise only)

### Billing Cycle
- Annual billing only
- Auto-renewal (can be disabled)
- Prorated refunds within 30 days

### Invoicing
- Invoices emailed immediately
- PDF format
- VAT/Tax included where applicable

### Renewal
- Automatic renewal 7 days before expiration
- Email reminder 30 days before
- Grace period: 14 days after expiration

---

## 🛡️ License Terms

### Professional/Enterprise Restrictions
- **No redistribution** - Plugin binaries are proprietary
- **Non-transferable** - License tied to user/organization
- **No reverse engineering** - Premium features are closed-source
- **Audit rights** - Enterprise customers subject to usage audits

### Community Edition
- **Open source** - Full source code available on GitHub
- **Apache 2.0 License** - Permissive commercial use
- **No warranty** - Provided "AS IS"
- **Contribution welcome** - PRs accepted

### Termination
- Violation of terms → immediate termination
- Non-payment → 14-day grace period
- Refund policy: 30 days, no questions asked

---

## 🌍 Regional Pricing

All prices in USD. Local currency pricing available:

| Region | Professional | Enterprise |
|--------|--------------|------------|
| 🇺🇸 North America | $99/year | $499/year |
| 🇪🇺 Europe | €89/year | €449/year |
| 🇬🇧 UK | £79/year | £399/year |
| 🇦🇺 Australia | A$139/year | A$699/year |
| 🇮🇳 India | ₹7,999/year | ₹39,999/year |
| 🇨🇳 China | ¥699/year | ¥3,499/year |

**Note**: Prices include applicable VAT/GST

---

## 📞 Contact Sales

### Professional Licenses
- **Email**: jakub.jirak@pm.me
- **Response time**: 24 hours

### Enterprise Licenses
- **Email**: jakub.jirak@pm.me
- **Phone**: Available after initial contact
- **Response time**: 4 hours (business days)

### Partnership Inquiries
For Intel partnership, training partnerships, or reseller agreements:
- **Email**: jakub.jirak@pm.me
- **Subject**: "Partnership Inquiry - [Company Name]"

---

## ❓ FAQ

### Can I use Community Edition commercially?
**Yes!** Apache 2.0 license allows commercial use without restrictions.

### Do I need a license per seat or per machine?
Professional is **per-user** (use on multiple machines). Enterprise is **per-organization** (unlimited users).

### Can I purchase monthly?
No, annual billing only. 30-day trial available for Professional.

### What happens when my license expires?
Premium features become view-only for 30 days, then disabled. Community features continue working.

### Can I get a refund?
Yes, full refund within 30 days of purchase, no questions asked.

### Do students get a discount?
Yes! 50% off Professional with valid .edu email ($49/year).

### Can I switch between editions?
Yes, upgrade anytime (prorated). Downgrade takes effect at renewal.

### Is source code available?
Community Edition: Yes (GitHub). Professional/Enterprise: Premium features are closed-source.

---

Built with ❤️ for heterogeneous computing developers

**Apache 2.0 License** - https://github.com/jakubjirak/dpcpp-clion-plugin/blob/main/LICENSE
