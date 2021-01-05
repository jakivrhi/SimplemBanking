# SimplemBanking

Task
The goal is to create a simple mBanking application that will allow the user to register, login and view
transaction on the selected account.
 
User registration
The screen should contain fields for entering the user's first and last name. Once the user defines that data
the next step is to define the pin. The pin is defined within a dialog that contains an entry display field and custom
numeric keypad through which we enter the pin. If we have successfully entered the pin, we will retrieve the user data
from the link: http://mobile.asseco-see.hr/builds/ISBD_public/Zadatak_1.json and show them on the following
screen.
 
Remark:
 All fields are required
 User name: Alphanumeric characters, max 30 characters
 Last name of the user: Alphanumeric characters, max 30 characters
 Pin: min 4, max 6 digits (native keyboard not used)
Overview of transactions by account
On the specified screen, we display the user's account and below it a list of transactions for that account. User
has the ability to select another account via dialog and display accordingly accordingly
transactions for that same account.
 
Remark:
 sort transactions by date
 enable user logout after which the login screen will be displayed

User login
The Login screen contains the title with the user's name and surname and the "Login" button where clicking on it opens
pin input dialog. If the pin is ok, the screen for displaying transactions by account opens.
The goal of the task is not just a functional application but an insight into the code and the overall solution.
Important things to take care of:
- Code sustainability
- Code transparency
- Extending the application with additional features
- UI / UX
