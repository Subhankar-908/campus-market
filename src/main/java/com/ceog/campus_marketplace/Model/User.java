package com.ceog.campus_marketplace.Model;


import com.ceog.campus_marketplace.Model.Type.RolesType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    // User.java — add field
    private String mobile;
    private String college;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<RolesType> roles=new HashSet<>();

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"seller", "category"})  // Ignore Product's back-references to prevent cycles
    private List<Product> products;


    @OneToMany(mappedBy = "reporter",cascade =CascadeType.ALL)
    @JsonIgnoreProperties({"reporter", "reportedUser"})  // Ignore Report's back-references
    private List<Report> reportsMade;


    @OneToMany(mappedBy = "reportedUser",cascade =CascadeType.ALL)
    @JsonIgnoreProperties({"reporter", "reportedUser"})  // Ignore Report's back-references
    private List<Report> reportReceive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(roles->new SimpleGrantedAuthority("ROLE_"+roles.name()))
                .collect(Collectors.toSet());
    }

//    @Override
//    public String getUsername() {
//        return "";
//    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(roles->new SimpleGrantedAuthority("ROLE_"+roles.name()))
//                .collect(Collectors.toSet());
//
//    }


}
