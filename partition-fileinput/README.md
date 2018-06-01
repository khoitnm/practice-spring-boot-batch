The sample code for Batch Partition which I copied and edited from https://github.com/walkingtechie/spring-batch-partition

Some updating code:
- Replace MySQL by an Embedded DB.
- Fix error when missing logger or setter/getter fields.
- Change DB Input to File Input: the Batch will read data from a file instead of a DB.
