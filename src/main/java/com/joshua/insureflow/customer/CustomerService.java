package com.joshua.insureflow.customer;

import com.joshua.insureflow.customer.dto.CustomerRequest;
import com.joshua.insureflow.customer.dto.CustomerResponse;
import com.joshua.insureflow.exception.BusinessRuleException;
import com.joshua.insureflow.exception.DuplicateResourceException;
import com.joshua.insureflow.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

@Service
public class CustomerService {

    private static final int MINIMUM_AGE = 18;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String phone = request.phoneNumber().trim();

        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("A customer with this email already exists");
        }
        if (customerRepository.existsByPhoneNumber(phone)) {
            throw new DuplicateResourceException("A customer with this phone number already exists");
        }
        if (Period.between(request.dateOfBirth(), LocalDate.now()).getYears() < MINIMUM_AGE) {
            throw new BusinessRuleException("Customer must be at least " + MINIMUM_AGE + " years old");
        }

        Customer customer = new Customer(
                request.firstName().trim(),
                request.lastName().trim(),
                email,
                phone,
                request.dateOfBirth());

        return toResponse(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return toResponse(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getDateOfBirth(),
                customer.getCreatedAt());
    }
}
